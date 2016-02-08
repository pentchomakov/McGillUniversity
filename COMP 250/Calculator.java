package a3;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.hamcrest.core.SubstringMatcher;

/**
 * Main class for the calculator: creates the GUI for the calculator 
 * and responds to presses of the "Enter" key in the text field 
 * and clicking on the button. You do not need to understand or modify 
 * the GUI code to complete this assignment. See instructions below the line
 * BEGIN ASSIGNMENT CODE
 * 
 * @author Martin P. Robillard 26 February 2015
 *
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame implements ActionListener
{
	private static final Color LIGHT_RED = new Color(214,163,182);
	
	private final JTextField aText = new JTextField(40);
	
	public Calculator()
	{
		setTitle("COMP 250 Calculator");
		setLayout(new GridLayout(2, 1));
		setResizable(false);
		add(aText);
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aText.setText("");		
				aText.requestFocusInWindow();
			}
		});
		add(clear);
		
		aText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		aText.addActionListener(this);

		aText.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();	
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				aText.getHighlighter().removeAllHighlights();
			}
		});
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Run this main method to start the calculator
	 * @param args Not used.
	 */
	public static void main(String[] args)
	{
		new Calculator();
	}
	
	/* 
	 * Responds to events by the user. Do not modify this method.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( aText.getText().contains("="))
		{
			aText.setText("");		
		}
		else
		{
			Queue<Token> expression = processExpression(aText.getText());
			if( expression != null )
			{
				String result = evaluateExpression(expression);
				if( result != null )
				{
					aText.setText(aText.getText() + " = " + result);
				}
			}
		}
	}
	
	/**
	 * Highlights a section of the text field with a color indicating an error.
	 * Any change to the text field erase the highlights.
	 * Call this method in your own code to highlight part of the equation to 
	 * indicate an error.
	 * @param pBegin The index of the first character to highlight.
	 * @param pEnd The index of the first character not to highlight.
	 */
	public void flagError( int pBegin, int pEnd )
	{
		assert pEnd > pBegin;
		try
		{
			aText.getHighlighter().addHighlight(pBegin, pEnd, new DefaultHighlighter.DefaultHighlightPainter(LIGHT_RED));
		}
		catch(BadLocationException e)
		{
			
		}
	}
	
	/******************** BEGIN ASSIGNMENT CODE ********************/
	
	/**
	 * Tokenizes pExpression (see Tokenizer, below), and 
	 * returns a Queue of Tokens that describe the original 
	 * mathematical expression in reverse Polish notation (RPN).
	 * Flags errors and returns null if the expression
	 * a) contains any symbol except spaces, digits, round parentheses, or operators (+,-,*,/)
	 * b) contains unbalanced parentheses
	 * c) contains invalid combinations of parentheses and numbers, e.g., 2(3)(4)
	 * 
	 * @param pExpression A string.
	 * @return The tokenized expression transformed in RPN
	 */
	String tokenizedRPN;
	
	private Queue<Token> processExpression(String pExpression) 
	{
		Queue<Token> tokens = new LinkedList<Token>();
		Tokenizer tokenizer = new Tokenizer();	
		Stack<Character> stack = new Stack<Character>();	//use stack to check for proper amount of parentheses
		char c;
		boolean setFlag = false;	//boolean to set flags return null 
		
		try{
			tokenizer.tokenize(pExpression);	//catch exceptions thrown in validate below in the tokenizer class
		}
		catch (InvalidExpressionException e){	
			flagError(e.getPosition(), e.getPosition()+1);	//when an exception is caught, that means there is an error in the inputted string
			setFlag = true;	//set flag to true for if condition later
		}
		
		for (int i = 0; i < pExpression.length(); i++){	//iterate through the string
			c = pExpression.charAt(i);	
				if (c == '('){	
					stack.push(c);	//if char at i is (, push onto stack
				}
				else if (c == ')'){
					if (stack.empty()){		//if there is a ) before any (, there is an error
						flagError(i, i+1);
						setFlag=true;
					}
					else if (stack.peek() == '('){	//if there is a ) after any (, pop stack
						stack.pop();
					}
				}
		}
		
		if (setFlag == true){	//check boolean to see if anything is flagged and needs to be highlighted
			return null;
		}		
		Queue<stringToken> tokensRPN = new LinkedList<stringToken>();	//initialize new queue for stringToken subclass
		tokens = tokenizer.getQueue();
		int queueSize = tokens.size();	//make sure the size stays constant since we're polling the queue
		
		for (int i = 0; i < queueSize; i++){
			int start = tokens.peek().getStart();	//start and end have the same value, so start needs to be peek, not poll
			int end = tokens.poll().getEnd();
			stringToken newToken = new stringToken(start, end, pExpression.substring(start, end+1));	//initialize a new stringToken for every iteration with using substrings of pExpression
			tokensRPN.add(newToken); //add each stringToken to a queue
		}
		
		String rpnExpression = RPN(tokensRPN);	//change the elements of the stringToken to a RPN format string
		tokenizedRPN = rpnExpression;	//expand the scope to the entire class
		return tokens; 
	}

	/**
	 * Assumes pExpression is a Queue of tokens produced as the output of processExpression.
	 * Evaluates the answer to the expression. The division operator performs a floating-point 
	 * division. 
	 * Flags errors and returns null if the expression is an invalid RPN expression e.g., 1+-
	 * @param pExpression The expression in RPN
	 * @return A string representation of the answer)
	 */
		
	private String evaluateExpression(Queue<Token> pExpression)	//we didn't use the queue of tokens here, instead we used the string tokenizedRPN in the scope of the class to simplify things 
	{
		double answer = evaluateRPN(tokenizedRPN);	
		String strAnswer = String.valueOf(answer);	
		return strAnswer; 
	}
	
	public double evaluateRPN(String pExpression) {
		double answer = 0.0;
	    String operators = "+-*/";
	    String [] tokens = pExpression.split("\\s+");	//RPN notation will be A B + E - F ... there will always be whitespace between each element
	    Stack<String> stack = new Stack<String>();	
	 
	    for(String tkn : tokens){	//iterate through each element of the array
	        if(!operators.contains(tkn)){	//if the nth element is not an operator, push it on the stack
	            stack.push(tkn);
	        }
	        else{
	        	double a = Double.valueOf(stack.pop());	//get values that will be operated on 
	            double b = Double.valueOf(stack.pop());	
	            int index = operators.indexOf(tkn);	//get type of operator (index 0 = +, index 1 = -, index 2 = *, index 3 = /)
	            switch(index){
	                case 0:
	                    stack.push(String.valueOf(a+b));	//apply operations based on index (type of operator)
	                    break;								//then push the value on the stack to become a new value that will be operated on
	                case 1:
	                   stack.push(String.valueOf(b-a));
	                   break;
	                case 2:
	                   stack.push(String.valueOf(a*b));
	                   break;
	                case 3:
	                   stack.push(String.valueOf(b/a));
	                   break;
	            }
	        }
	    }
	    answer = Double.valueOf(stack.pop());	//the answer will always be the last remaining value on the stack
	    return answer;
	}

	private enum Operators
    {
		add(1), sub(2), mult(3), div(4);
		int precedence;
		Operators(int p){
        	precedence = p;
        }
    }

    private static HashMap<String, Operators> Ops = new HashMap<String, Operators>() {{ //use a hashmap to change to RPN
        put("+", Operators.add);
        put("-", Operators.sub);
        put("*", Operators.mult);
        put("/", Operators.div);
    }};

    private static boolean isHigherPrecedence(String operator, String substring) //check the precedence of the two strings compared
    {
        return (Ops.containsKey(substring) && Ops.get(substring).precedence >= Ops.get(operator).precedence);
    }
    

	public static String RPN(Queue<stringToken> rpnTokens){
		StringBuilder outputString = new StringBuilder();	//build string to be returned
		Deque<String> stack = new LinkedList<String>();		//use stack to separate numbers and operators
		ArrayList<String> tempArray = new ArrayList<String>();	//temporary array to swap from stringTokens to Strings
		
		while(!rpnTokens.isEmpty()){	//poll string values from rpnTokens to the arraylist until rpnTokens is empty
			tempArray.add(rpnTokens.poll().getString());
		}
		
		for (int i = 0; i < tempArray.size(); i++){	 
			String token = tempArray.get(i);	
			if (Ops.containsKey(token)){	//if element i is an operator
				while (!stack.isEmpty() && isHigherPrecedence(token, stack.peek())){	//if it isnt the first element and if it has higher precendence than the previous element of the stack
					outputString.append(stack.pop()).append(" ");	//build string
				}
				stack.push(token);	//push operator onto stack
			}
			else if (token.equals("(")){	//push onto stack to get proper order of operation
				stack.push(token);
			}
			else if (token.equals(")")){
				while (!stack.peek().equals("(")){
					outputString.append(stack.pop()).append(" ");	//build string
				}
				stack.pop();	//pop whenever () is complete
			}
			else{
				outputString.append(token).append(" ");	//append other elements to the string
			}
		}
		
		while(!stack.isEmpty()){
			outputString.append(stack.pop()).append(" ");	//pop all remaining elements and add to the string
		}
		return outputString.toString();
	}
}

/**
 * Use this class as the root class of a hierarchy of token classes
 * that can represent the following types of tokens:
 * a) Integers (e.g., "123" "4", or "345") Negative numbers are not allowed as inputs
 * b) Parentheses '(' or ')'
 * c) Operators '+', '-', '/', '*' Hint: consider using the Comparable interface to support
 * comparing operators for precedence
 */
class Token
{
	private int aStart;
	private int aEnd;
	
	/**
	 * @param pStart The index of the first character of this token
	 * in the original expression.
	 * @param pEnd The index of the last character of this token in
	 * the original expression
	 */
	public Token( int pStart, int pEnd )
	{
		aStart = pStart;
		aEnd = pEnd;
	}
	
	public int getStart()
	{
		return aStart;
	}
	
	public int getEnd()
	{
		return aEnd;
	}
	
	public String toString()
	{
		return "{" + aStart + "," + aEnd + "}";
	}
}

class stringToken extends Token{	//subclass to add a string value to each token 
	
	private int aStart;
	private int aEnd;
	private String substring;
	
	public stringToken(int pStart, int pEnd, String sub) {
		super(pStart, pEnd);
		aStart = pStart;
		aEnd = pEnd;
		substring = sub;
	}
	
	public String toString(){
		return "{" + aStart + "," + aEnd + "," + substring + "}"; 
	}
	
	public String getString(){
		return substring;
	}
	
	public int getStart(){
		return aStart;
	}

	public int getEnd(){
		return aEnd;
	}
}
/**
 * Partial implementation of a tokenizer that can convert any valid string
 * into a stream of tokens, or detect invalid strings. Do not change the signature
 * of the public methods, but you can add private helper methods. The signature of the
 * private methods is there to help you out with basic ideas for a design (it is strongly 
 * recommended to use them). Hint: consider making your Tokenizer an Iterable<Token>
 */
class Tokenizer
{
	/**
	 * Converts pExpression into a sequence of Tokens that are retained in
	 * a data structure in this class that can be made available to other objects.
	 * Every call to tokenize should clear the structure and start fresh.
	 * White spaces are tolerated but should be ignored (not converted into tokens).
	 * The presence of any illegal character should raise an exception.
	 * 
	 * @param pExpression An expression to tokenize. Can contain invalid characters.
	 * @throws InvalidExpressionException If any invalid character is detected or if parentheses
	 * are misused as operators.
	 */
	
	Queue<Token> tokens = new LinkedList<Token>();	//class variable to store the queue of tokens
	String validateString = new String();	//class variable to store pExpression

	public void tokenize(String pExpression) throws InvalidExpressionException
	{
		Token newToken;
		int start;
		int end;
		validateString = pExpression;	//copy value of pExpression
		for (int i = 0; i < pExpression.length(); i++){
			char x = pExpression.charAt(i);
			if (x == '+' || x == '-' || x == '*' || x == '/' || x == '(' || x == ')'){	//separate all operators into indivdual tokens and add them to the queue
				newToken = new Token(i, i);	
				tokens.add(newToken);	
			}
			else if (Character.isDigit(x)){	//if its a digit, digits one after the other form one token and are added to the queue
				int temp = i;
				if (temp < pExpression.length()-1){ 
					while(Character.isDigit(pExpression.charAt(temp+1))){
						temp++;
						if (temp == pExpression.length()-1){
							break;
						}
					}
				}
				start = i;
				end = temp;
				newToken = new Token (start, end);
				tokens.add(newToken);
				i = end;
				if (i == pExpression.length())	
					break;
			}
			else{
				continue;	//if the input is anything else, do not create token and ignore it
			}
		}
		validate();
	}
	
	public Queue<Token> getQueue(){
		return tokens;
	}
	
	public String getExpression(){
		return validateString;
	}
	
	/*private void consume(char pChar) throws InvalidExpressionException
	{
		// Consume a single character in the input expression and deals with
		// it appropriately.
		// We did not use this function, everything is implemented in Tokenize
	}*/
	
	/**
	 * Detects if parentheses are misused
	 * @throws InvalidExpressionException
	 */
	private void validate() throws InvalidExpressionException	//throws expressions that will be caught in the processExpression method, which will also determine the index for flags
	{
		// An easy way to detect if parentheses are misused is 
		// to look for any opening parenthesis preceded by a token that
		// is neither an operator nor an opening parenthesis, and for any
		// closing parenthesis that is followed by a token that is
		// neither an operator nor a closing parenthesis. Don't check for
		// unbalanced parentheses here, you can do it in processExpression
		// directly as part of the Shunting Yard algorithm.
		// Call this method as the last statement in tokenize.
		
		for (int i = 0; i < validateString.length(); i++){
			if (!Character.toString(validateString.charAt(i)).matches("[-0-9()+*/]")){	//if the string does not contain any valid inputs
				throw new InvalidExpressionException(i);
			}
			else if (validateString.charAt(i) == '('){	//if the character is (, it cannot be preceded by a digit or a ), and cannot be followed by an operator
				if (i < validateString.length()-1){		//it also cannot be the last character in the string
					if(i != 0){
						if (validateString.charAt(i-1) != '(' && validateString.charAt(i-1) != '+' && validateString.charAt(i-1) != '-' && validateString.charAt(i-1) != '*' && validateString.charAt(i-1) != '/'){
							throw new InvalidExpressionException(i-1);
						}
						else if (Character.isDigit(validateString.charAt(i+1)) != true && validateString.charAt(i+1) != '('){
							throw new InvalidExpressionException(i+1);
						}
					}
					else if (Character.isDigit(validateString.charAt(i+1)) != true && validateString.charAt(i+1) != '('){
						throw new InvalidExpressionException(i+1);
					}
				}	
				else if (i == validateString.length()-1){
					throw new InvalidExpressionException(i);
				}
				else if (i == 1){
					throw new InvalidExpressionException(i);
				}
			}
			else if (validateString.charAt(i) == ')'){				//if the character is ), it cannot be followed by a digit or a (, and cannot be preceded by an operator
				if (i < validateString.length()-1 && i != 0){		//it also cannot be the first character in the string
					if (validateString.charAt(i+1) != ')' && validateString.charAt(i+1) != '+' && validateString.charAt(i+1) != '-' && validateString.charAt(i+1) != '*' && validateString.charAt(i+1) != '/'){
						throw new InvalidExpressionException(i+1);
					}
					else if (Character.isDigit(validateString.charAt(i-1)) != true && validateString.charAt(i-1) != ')'){
						throw new InvalidExpressionException(i-1);
					}
				}
				else if (i == 0){
					throw new InvalidExpressionException(i);
				}
				else if (i == validateString.length()-2){
					throw new InvalidExpressionException(i);
				}
			}
			else if (validateString.charAt(i) == '+' || validateString.charAt(i) == '-' || validateString.charAt(i) == '*' || validateString.charAt(i) == '/'){
				if (i < validateString.length()-1 && i != 0){
					if (validateString.charAt(i+1) != '(' && Character.isDigit(validateString.charAt(i+1)) == false){
						throw new InvalidExpressionException(i+1);
					}
					else if (validateString.charAt(i-1) != ')' && Character.isDigit(validateString.charAt(i-1)) == false){
						throw new InvalidExpressionException(i-1);
					}
				}
				else if (i == 0 || i == validateString.length()-1){
					throw new InvalidExpressionException(i);
				}
			}
		}
	}	
}
/**
 * Thrown by the Tokenizer if an expression is detected to be invalid.
 * You don't need to modify this class.
 */
@SuppressWarnings("serial")
class InvalidExpressionException extends Exception
{
	private int aPosition;
	
	public InvalidExpressionException( int pPosition )
	{
		aPosition = pPosition;
	}
	
	public int getPosition()
	{
		return aPosition;
	}
}
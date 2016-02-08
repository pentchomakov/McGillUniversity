package assignment1;

import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;

 /* ACADEMIC INTEGRITY STATEMENT
 * 
 * By submitting this file, we state that all group members associated
 * with the assignment understand the meaning and consequences of cheating, 
 * plagiarism and other academic offenses under the Code of Student Conduct 
 * and Disciplinary Procedures (see www.mcgill.ca/students/srr for more information).
 * 
 * By submitting this assignment, we state that the members of the group
 * associated with this assignment claim exclusive credit as the authors of the
 * content of the file (except for the solution skeleton provided).
 * 
 * In particular, this means that no part of the solution originates from:
 * - anyone not in the assignment group
 * - Internet resources of any kind.
 * 
 * This assignment is subject to inspection by plagiarism detection software.
 * 
 * Evidence of plagiarism will be forwarded to the Faculty of Science's disciplinary
 * officer.
 */

/**
 * When you run the main method, the program should print the name of the professor
 * that is the best match for the keywords described in the QUERY array using two similarity
 * metrics: the Jaccard index and the relative number of keyword matches. If no result is found,
 * the program should print out: "No result found".
 *  
 *  Complete the code provided as part of the assignment package. 
 *  
 *  Complete the list of professors, but do not remove the ones who are there or their URL.
 *  
 *  You can change the content of the QUERY as you like.
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes.
 *  
 *  Do all the work using arrays. Do not use the Collection classes (List, etc.) The goal of 
 *  this assignment is to develop proficiency in the design of algorithm and the use of basic 
 *  data structures. 
 *  
 *  It is recommended to use the Arrays.sort and Arrays.binarySeach methods.
 *  
 *  Make sure your entire solution is in this file.
 *
 */
public class Assignment1
{
	/**
	 * List of professors to search. Complete the list with all professors in the school
	 * of computer science. Choose the page that has the best description of the professor's
	 * research interests.
	 */
	private static Professor[] PROFESSORS = {
		new Professor("Martin Robillard", "http://www.cs.mcgill.ca/~martin"),
		new Professor("Gregory Dudek", "http://www.cim.mcgill.ca/~dudek/dudek_bio.html"),
		new Professor("Yang Cai", "http://www.cs.mcgill.ca/~cai/"),
		new Professor("Brigitte Pientka", "http://www.cs.mcgill.ca/~bpientka/"),
		new Professor("Tim Merrett", "http://www.cs.mcgill.ca/~tim/"),
		new Professor("Monty Newborn", "http://www.cs.mcgill.ca/~newborn/"),
		new Professor("Gerald Ratzer", "http://www.cs.mcgill.ca/~ratzer/"),
		new Professor("Renato De Mori", "http://www.researchgate.net/profile/Renato_De_Mori"),
		new Professor("Chris Paige", "http://www.cs.mcgill.ca/~chris/"),
		new Professor("Godfried Toussaint", "http://www-cgrl.cs.mcgill.ca/~godfried/"),
		new Professor("Ioannis Rekleitis", "http://www.cim.mcgill.ca/~yiannis/"),
		new Professor("Adrian Vetta", "http://www.math.mcgill.ca/~vetta/"),
		new Professor("Jerome Waldispuhl", "http://www.cs.mcgill.ca/~jeromew/"),
		new Professor("Hans Vangheluwe", "http://msdl.cs.mcgill.ca/people/hv"),
		new Professor("Clark Verbrugge", "http://www.sable.mcgill.ca/~clump/"),
		new Professor("Denis Therien", "http://www.cs.mcgill.ca/~denis/"),
		new Professor("Carl Tropper", "http://www.cs.mcgill.ca/~carl/"),
		new Professor("Derek Ruths", "http://www.ruthsresearch.org/"),
		new Professor("Kaleem Siddiqi", "http://www.cim.mcgill.ca/~siddiqi/"),
		new Professor("Bruce Reed", "http://cgm.cs.mcgill.ca/~breed/"),
		new Professor("Joelle Pineau", "http://www.cs.mcgill.ca/~jpineau/"),
		new Professor("Doina Precup", "http://www.cs.mcgill.ca/~dprecup/"),
		new Professor("Prakash Panangaden", "http://www.cs.mcgill.ca/~prakash/"),
		new Professor("Xue Liu", "http://www.cs.mcgill.ca/~xueliu/"),
		new Professor("Muthucumaru Maheswaran", "http://www.cs.mcgill.ca/~maheswar/"),
		new Professor("Paul Kry", "http://www.cs.mcgill.ca/~kry/"),
		new Professor("Michael Langer", "http://www.cim.mcgill.ca/~langer/"),
		new Professor("Bettina Kemme", "http://www.cs.mcgill.ca/~kemme/"),
		new Professor("Jörg Kienzle", "http://www.cs.mcgill.ca/~joerg/Home/Jorgs_Home.html"),
		new Professor("Wenbo He", "http://www.cs.mcgill.ca/~wenbohe/"),
		new Professor("Laurie Hendren", "http://www.sable.mcgill.ca/~hendren/"),
		new Professor("Hamed Hatami", "http://www.cs.mcgill.ca/~hatami/"),
		new Professor("Luc Devroye", "http://luc.devroye.org/"),
		new Professor("Claude Crepeau", "http://www.cs.mcgill.ca/~crepeau/"),
		new Professor("Xiao-Wen Chang", "http://www.cs.mcgill.ca/~chang/"),
		new Professor("David Avis", "http://cgm.cs.mcgill.ca/~avis/")
	};
	
	/**
	 * A set of keywords describing an area of interest. Does not have to be sorted, 
	 * but must not contain any duplicates.
	 */
	private static String[] QUERY = {"game", "artificial", "software"};
	
	/**
	 * Words with low information content that we want to exclude from the similarity
	 * measure.
	 * 
	 * This array should always be sorted. Don't change anything for the assignment submissions,
	 * but afterwards if you want to keep playing with this code, there are some other words
	 * that would obviously be worth adding.
	 */
	private static String[] STOP_WORDS = {"a", "an", "and", "at", "by", "for", "from", "he", "his", 
		"in", "is", "it", "of", "on", "she", "the", "this", "to", "with"};
	
	/**
	 * Your program starts here. You should not need to do anything here besides
	 * removing the first two statements once you have copied the required statement
	 * and dealing with the case where there are no results.
	 */
	public static void main(String[] args) throws IOException{
		Arrays.sort(QUERY);
		System.out.println("The keywords we are searching for are: " + Arrays.toString(QUERY));
		System.out.println("The best match according to the Jaccard method is: " + bestMatchJaccard(QUERY));
		System.out.println("The best match according to the RelHits method is: " + bestMatchRelHits(QUERY));
	}
	
	/**
     * Returns the name of the professor that is the best match according to
     * the Jaccard similarity index, or the empty String if there are no such
     * professors. pQuery must not include duplicate or stop
     * words, and must be sorted before being passed into this function.
	 */
	public static String bestMatchJaccard(String[] pQuery) throws IOException
	{
		assert pQuery != null;
		String bestMatch = "No match found";	
		double currentJaccard = 0; 	
		
		//iterates through each professor's web page and finds the best match by setting an initial Jaccard value and then replacing it whenever there is a higher value
		//the best match will be at the index where the Jaccard value is replaced
		for(int i = 0; i < PROFESSORS.length; i++)
		{
			if(jaccardIndex(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())), pQuery) > currentJaccard)
			{
				currentJaccard = jaccardIndex(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())), pQuery);
				bestMatch = PROFESSORS[i].getName();
			}
			
		}
		return bestMatch; 
	}
	
	/**
	 * Returns the size of the intersection between pDocument and pQuery.
	 * pDocument can contain duplicates, pQuery cannot. Both arrays must 
	 * be sorted in alphabetical order before being passed into this function.
	 */
	public static int intersectionSize(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		int size = 0;
		
		//Remove duplicates from pDocument by replacing duplicates by 0, changing the string array to a string, using the replaceAll method to remove 0's and unnecessary characters and then changing it back to a string array
		
		for (int i = 0; i < pDocument.length; i++)
		{
			if (i + 1 < pDocument.length && pDocument[i].equals(pDocument[i+1]))
			{
				pDocument[i] = "0";
			}
		}
		String DocumentString = Arrays.toString(pDocument).replaceAll("[^ a-zA-Z�����]", "").trim();
		String [] Document = DocumentString.split("\\s+");
	
		//nested for loops to compare elements of both lists and see if they match
		for (int i = 0; i < pQuery.length; i++ )
		{
			for (int j = 0; j < Document.length; j++)
			{
				if (Document[j].equals(pQuery[i]))
				{
					size++;
				}
			}
		}
		
 		return size; 
	}
	
	/**
     * Returns the name of the professor that is the best match according to
     * the RelHits (relative hits) similarity index, computed as numberOfHits/size of the document.
     * Returns the empty string if no professor is found.
     * pQuery must not include duplicate or stop words, and must be sorted before
     * being passed into this function.
	 */
	public static String bestMatchRelHits(String[] pQuery) throws IOException
	{
		assert pQuery != null;
		String bestMatch = "No match found";
		double currentRelHits = 0; 
		
		//take the number of hits of each professor's page by getting their url, extracting the words, removing stopwords
		//divide it by the amount of significant words to find relative hits
		for(int i = 0; i < PROFESSORS.length; i++)
		{
			if ((double) numberOfHits(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())), pQuery)/(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())).length) > currentRelHits)
			{
				currentRelHits = (double) numberOfHits(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())), pQuery)/(removeStopWords(obtainWordsFromPage(PROFESSORS[i].getWebPageUrl())).length);
				bestMatch = PROFESSORS[i].getName();
			}
		}
		
		return bestMatch; 
	}
	
	/**
	 * Returns the Jaccard similarityIndex between pDocument and pQuery,
	 * that is, |intersection(pDocument,pQuery)|/|union(pDocument,pQuery)|
	 */
	public static double jaccardIndex(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		double jaccard;
		
		double intersection = intersectionSize(pDocument, pQuery);
		double union = unionSize(pDocument, pQuery);
		jaccard = (intersection/union)*100;
		return jaccard; 
	}
	
	/**
	 * Returns the size of the union between pDocument and pQuery. 
	 * pDocument can contain duplicates, pQuery cannot. Both arrays must 
	 * be sorted in alphabetical order before being passed into this 
	 * function.
	 */
	public static int unionSize(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		String [] pUnion = new String[pDocument.length + pQuery.length];
		
		//merge the two arrays of strings and sort them   
		for (int i = 0; i < pDocument.length; i++)
		{
			pUnion [i] = pDocument [i];
		}
		for (int i = 0; i < pQuery.length; i++)
		{
			pUnion [i + pDocument.length] = pQuery[i];
		}
		Arrays.sort(pUnion);
		
		//remove duplicates by changing duplicate array elements to 0, changing the array to a string, replaceAll method to remove unnecessary symbols and 0's, changing the string back to an array
		for (int i = 0; i < pUnion.length; i++)
		{
			if (i + 1 < pUnion.length && pUnion[i].equals(pUnion[i+1]))
			{
				pUnion[i] = "0";
			}
		}
		
		String UnionString = Arrays.toString(pUnion).replaceAll("[^ a-zA-Z�����]", "").trim();
		String [] Union = UnionString.split("\\s+");
		int size = Union.length; 
		return size; 
	}
	
	/**
	 * Returns the number of times that any word in pQuery is found in pDocument
	 * for any word, and including repetitions. For example, if pQuery contains 
	 * "design" and "design" is found 3 times in pDocument, this would return 3.
	 * Both pDocument and pQuery should be sorted in alphabetical order before 
	 * being passed into this function.
	 */
	
	public static int numberOfHits(String[] pDocument, String[] pQuery)
	{
		assert pDocument != null && pQuery != null;
		
		// BinarySearch to find every time there is a hit
		int hits = 0;
		for(int i = 0; i < pDocument.length; i++){
			if(Arrays.binarySearch(pQuery,  pDocument[i]) >= 0){
				hits++;
			}
		}
		
		return hits; 
	}
	
	/**
	 * Returns a new array of words that contains all the words in pKeyWords
	 * that are not in the array of stop words. The order of the original 
	 * array should not be modified except by removing words. If the array is sorted,
	 * the resulting array should also be sorted.
	 * @param pKeyWords The array to trim from stop words
	 * @return A new array without the stop words.
	 */
	public static String[] removeStopWords(String[] pKeyWords)
	{
		assert pKeyWords != null;
		
		//replace stop words in the array by 0, change the array to a string, replaceAll method to remove symbols and 0's, and then change the string back to an array
		for (int i = 0; i < STOP_WORDS.length; i++)
		{
			for (int j = 0; j < pKeyWords.length; j++)
			{
				if (STOP_WORDS[i].equals(pKeyWords[j]))
				{
					pKeyWords[j] = "0";
				}
			}
		}
		String allWords = Arrays.toString(pKeyWords);
		allWords = allWords.trim();
		String [] cleanWords = allWords.replaceAll("[^ a-zA-Z]", "").trim().split("\\s+");
		return cleanWords;
	}
	
	/**
	 * Obtains all the words in a page (including duplicates), but excluding punctuation and
	 * extraneous whitespaces and tabs. The results should be sorted in alphabetical order
	 * and all be completely in lower case.
	 * Consider using String.replaceAll(...) to complete this method.
	 * @throws IOException if we can't download the page (e.g., you're off-line)
	 */
	public static String[] obtainWordsFromPage(String pUrl) throws IOException
	{		
		String inputString = Jsoup.connect(pUrl).get().text();
		String words[] = inputString.replaceAll("[^ a-zA-Z�����]", "").toLowerCase().split("\\s+");
		Arrays.sort(words);
        return words;
	}
}

/**
 * This simple class just keeps the information about
 * a professor together. Do not modify this class.
 */
class Professor
{
	private String aName;
	private String aWebPageUrl; 
	
	public Professor(String pName, String pWebpageUrl)
	{
		aName = pName;
		aWebPageUrl = pWebpageUrl;
	}
	
	public String getName()
	{
		return aName;
	}
	
	public String getWebPageUrl()
	{
		return aWebPageUrl;
	}
}

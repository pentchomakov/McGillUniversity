// Pentcho Tchomakov, Ming Cao

package a4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/* ACADEMIC INTEGRITY STATEMENT
 * 
 * Paste the block here.
 */



/* A Simple Search Engine exploring subnetwork of McGill University's webpages.
 * 	
 *	Complete the code provided as part of the assignment package. Fill in the \\TODO sections
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes. Do not import any data structures. 
 *  
 *  Make sure your entire solution is in this file.
 *  
 *  We have simplified the task of exploring the network. Instead of doing the search online, we've
 *  saved the result of an hour of real-time graph traversal on the McGill network into two csv files.
 *  The first csv file "vertices.csv" contains the vertices (webpages) on the network and the second csv 
 *  file "edges.csv" contains the links between vertices. Note that the links are directed edges.
 *  
 *  An edge (v1,v2) is link from v1 to v2. It is NOT a link from v2 to v1.
 * 
 */

public class Search {

	private static ArrayList<Vertex> graph; //= new ArrayList<Vertex>();
	private ArrayList<Vertex> BFS_inspector;
	private ArrayList<Vertex> DFS_inspector;
	private Comparator<SearchResult> comparator = new WordOccurrenceComparator();
	private PriorityQueue<SearchResult> wordOccurrenceQueue;
	
	/**
	 * You don't have to modify the constructor. It only initializes the graph
	 * as an arraylist of Vertex objects
	 */
	public Search(){
		graph = new ArrayList<Vertex>();
	}
	
	/**
     * Used to invoke the command line search interface. You only need to change
     * the 2 filepaths and toggle between "DFS" and "BFS" implementations.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String pathToVertices = "vertices.csv";
		String pathToEdges = "edges.csv";
		
		//loadGraph(pathToVertices, pathToEdges);
		Search mcgill_network = new Search();
		mcgill_network.loadGraph(pathToVertices, pathToEdges);
		
		Scanner scan = new Scanner(System.in);
		String keyword;
		
		do{
			System.out.println("\nEnter a keyword to search: ");
			keyword = scan.nextLine();
			
			if(keyword.compareToIgnoreCase("EXIT") != 0){
				mcgill_network.search(keyword, "BFS");		//You should be able to change between "BFS" and "DFS"
				mcgill_network.displaySearchResults();
			}

		} while(keyword.compareToIgnoreCase("EXIT") != 0);
		
		System.out.println("\n\nExiting Search...");
		scan.close();
	}
	
	/**
	 * Do not change this method. You don't have to do anything here.
	 * @return
	 */
	public int getGraphSize(){
		return this.graph.size();
	}
	
	/**
	 * This method will either call the BFS or DFS algorithms to explore your graph and search for the
	 * keyword specified. You do not have to implement anything here. Do not change the code.
	 * @param pKeyword
	 * @param pType
	 */
	public void search(String pKeyword, String pType){
			resetVertexVisits();
			wordOccurrenceQueue = new PriorityQueue<SearchResult>(1000, comparator);
			BFS_inspector = new ArrayList<Vertex>();
			DFS_inspector = new ArrayList<Vertex>();
			
			if(pType.compareToIgnoreCase("BFS") == 0){
				Iterative_BFS(pKeyword);
			}
			else{
				Iterative_DFS(pKeyword);
			}
	}
	
	/**
	 * This method is called when a new search will be performed. It resets the visited attribute
	 * of all vertices in your graph. You do not need to do anything here.
	 */
	public void resetVertexVisits(){
		for(Vertex k : graph){
			k.resetVisited();
		}
	}
	
	/**
	 * Do not change the code of this method. This is used for testing purposes. It follows the 
	 * your graph search traversal track to ensure a BFS implementation is performed.
	 * @return
	 */
	public String getBFSInspector(){
		String result = "";
		for(Vertex k : BFS_inspector){
			result = result + "," + k.getURL();
		}
		
		return result;
	}
	
	/**
	 * Do not change the code of this method. This is used for testing purposes. It follows the 
	 * your graph search traversal track to ensure a DFS implementation is performed.
	 * @return
	 */
	public String getDFSInspector(){
		String result = "";
		for(Vertex k : DFS_inspector){
			result = result + "," + k.getURL();
		}
		return result;
	}
	
	/**
	 * This method prints the search results in order of most occurrences. It utilizes
	 * a priority queue (wordOccurrenceQueue). You do not need to change the code.
	 * @return
	 */
	public int displaySearchResults(){
		
		int count = 0;
		while(this.wordOccurrenceQueue.size() > 0){
			SearchResult r = this.wordOccurrenceQueue.remove();
			
			if(r.getOccurrence() > 0){
				System.out.println("Count: " + r.getOccurrence() + ", Page: " + r.getUrl());
				count++;
			}
		}
		
		if(count == 0) System.out.println("No results found for your search query");
		
		return count;
		
	}
	
	/**
	 * This method returns the graph instance. You do not need to change the code.
	 * @return
	 */
	public ArrayList<Vertex> getGraph(){
		return this.graph;
	}
	
	/**
	 * This method takes in the 2 file paths and creates your graph. Each Vertex must be 
	 * added to the graph arraylist. To implement an edge (v1, v2), add v2 to v1.neighbors list
	 * by calling v1.addNeighbor(v2)
	 * @param pVerticesPathFile
	 * @param pEdgesFilePath
	 * @throws IOException 
	 */
	public static void loadGraph(String pVerticesFilePath, String pEdgesFilePath) throws IOException{
		
		// **** LOADING VERTICES ***///
		// Creating two seperate Readers to load each file seperately. 
		BufferedReader br1 = new BufferedReader(new FileReader(pVerticesFilePath));
		String currentLine1 = "";
		BufferedReader br2 = new BufferedReader(new FileReader(pEdgesFilePath));
		String currentLine2 = "";

		try{
			// While there is a next line, loop the following...
			while((currentLine1 = br1.readLine()) != null){
				// We import the current line into a list by splitting at every comma since the file is of type CSV
				List<String> itemsInRowVertices = Arrays.asList(currentLine1.split(","));
				// We initialize a new vertex for the current line. Every current line will get a new vertex since every row represents a vertex.
				// We also give every vertex its path which is in the position 0 in the list.
				Vertex vertex = new Vertex(itemsInRowVertices.get(0));
				
				// We add all the remaining words, starting from position 1, into an ArrayList of words that is a parameter of the Vertex Object.
				for(int i = 1; i < itemsInRowVertices.size(); i++){
					vertex.addWord(itemsInRowVertices.get(i));
				}
				// We finalize by adding the vertex to the graph and we loop..
				graph.add(vertex);
			}
			// **** END LOADING VERTICES ***///
			
			// **** LOADING EDGES ***///
			// We loop through every line (row). Every row represents a vertex v1 directed to a vertex v2 (edge (v1,v2)), arranged into two columns
			while((currentLine2 = br2.readLine()) != null){
				// We import both columns into a list using the same method as for the vertices
				List<String> itemsInRowEdges = Arrays.asList(currentLine2.split(","));

				// We do a cross section of two ArrayList to create the edges
				for(int i = 0; i < graph.size(); i++){
					// We check if the vertex v1 exists in the graph we are looking into
					if(graph.get(i).getURL().equals(itemsInRowEdges.get(0))){
						// If it does exist, we loop through the graph to see if the vertex v2 exists
						for(int j = 0; j < graph.size(); j++){
							if(graph.get(j).getURL().equals(itemsInRowEdges.get(1))){
								// If it also exists, then we direct our vertex v1 to vertex v2 to create an edge. v2 becomes a neighbor of v1
								graph.get(i).addNeighbor(graph.get(j));
							}
						}
					}
				}
			}
			// **** END LOADING EDGES ***///
			
		}
	
		catch (IOException e){
			System.out.println(e);
		}
		br1.close();
		br2.close();
	}
	
	
	/**
	 * This method must implement the Iterative Breadth-First Search algorithm. Refer to the lecture
	 * notes for the exact implementation. Fill in the //TODO lines
	 * @param pKeyword
	 */
	public void Iterative_BFS(String pKeyword){
		// Check if the input is null or if the String consists only of white spaces 
		if(pKeyword != null && pKeyword.matches(".*\\w.*")){
			ArrayList<Vertex> BFSQ = new ArrayList<Vertex>();	//This is your breadth-first search queue.
			Vertex start = graph.get(0);						//We will always start with this vertex in a graph search
			
			// We set the first vertex as visited and we add them to the queue, and test queue.
			start.setVisited();
			BFSQ.add(start);
			BFS_inspector.add(start);
			
			// Until there are no more vertices to go through, we loop through BFSQueue.
			while(!BFSQ.isEmpty()){
				// We create a  new ArrayList of all the neighbors of the first element of the queue.
				ArrayList<Vertex> vertexNeighbors = BFSQ.get(0).getNeighbors();
				// We also load all the words from that vertex into an ArrayList
				ArrayList<String> wordsFromVertex = BFSQ.get(0).getWords();
				
				// From the input "pKeyWord", we loop through every word of the ArrayList of words from the Vertex and we count how many times pKeyWord appears.
				int occurences = 0;
				for(int i = 0; i < wordsFromVertex.size(); i++){
					// We set pKeyWord to lower case because our csv file has only lower case words. That being said, we don't know if the csv ALWAYS has lower case words
					// and because of that, we also put the wordFromVertex into lower case to be 100% sure.
					if(wordsFromVertex.get(i).toLowerCase().contains(pKeyword.toLowerCase())){
						occurences++;
					}
				}
				// Then, for every vertex, we add a new search object which contains the URL and the number of occurrences of the search word pKeyWord.
				this.wordOccurrenceQueue.add(new SearchResult(BFSQ.get(0).getURL(), occurences));
				// Since are "done" extracting all the data we need from that vertex, we dequeue it.
				BFSQ.remove(0);
				// Here, we loop through every Neighbor of the vertex, and if that neighbor hasn't been visited, we set it as "visit 
				// AND we enqueue (add it at the end of the queue).
				for(int i = 0; i < vertexNeighbors.size(); i++){
					if(vertexNeighbors.get(i).getVisited() != true){
						// Every time there is a neighbor that hasn't been visited, we add it to our queue and inspection queue and we then flag it as "visited"
						BFSQ.add(vertexNeighbors.get(i));
						BFS_inspector.add(vertexNeighbors.get(i));
						vertexNeighbors.get(i).setVisited();
					}
				}
			}
		}
		// We tell the user that their input cannot be null or only made of white spaces
		else{
			System.out.println("You cannot enter an empty search selection");
			return;
		}
	}
	
	/**
	 * This method must implement the Iterative Depth-First Search algorithm. Refer to the lecture
	 * notes for the exact implementation. Fill in the //	
	 * @param pKeyword
	 */
	public void Iterative_DFS(String pKeyword){
		// Check if the input is null or if the String consists only of white spaces 
		if(pKeyword != null && pKeyword.matches(".*\\w.*")){
			Stack<Vertex> DFSS = new Stack<Vertex>();	//This is your depth-first search stack.
			Vertex start = graph.get(0);	
			
			// We set the starting vertex as "Visited"
			start.setVisited();
			DFSS.push(start); 
			DFS_inspector.add(start);
			
			while(!DFSS.isEmpty()){
				// We load all the neighbors of the given Vertex into an ArrayList of Vertices
				ArrayList<Vertex> vertexNeighbors = DFSS.peek().getNeighbors();
				// We load all the words from the given Vertex with an ArrayList of Strings
				ArrayList<String> wordsFromVertex = DFSS.peek().getWords();
				int occurrences = 0;
				
				// As for BFS, we count number of occurrences in pKeyword by going through the ArrayList of words of the given Vertex.
				for(int i = 0; i < wordsFromVertex.size(); i++){
					// We set pKeyWord to lower case because our csv file has only lower case words. That being said, we don't know if the csv ALWAYS has lower case words
					// and because of that, we also put the wordFromVertex into lower case to be 100% sure.
					if(wordsFromVertex.get(i).toLowerCase().contains(pKeyword.toLowerCase())){
						occurrences++;
					}
					
				}
				// We then add it to the wordOccurence queue as a new Search object containing an URL and the number of occurrences of pKeyword in the given vertex (webwpage/link).
				this.wordOccurrenceQueue.add(new SearchResult(DFSS.peek().getURL(), occurrences));
				// We remove the top element of the stack.
				DFSS.pop();
				// We loop for neighbors 
				for(int i = 0; i < vertexNeighbors.size(); i++){
					if(vertexNeighbors.get(i).getVisited() != true){
						// If the neighbor is not visited, we set it to "visited"
						vertexNeighbors.get(i).setVisited();
						// We then add that neighbor on top of the stack and we also add it to the inspector ArrayList
						DFSS.push(vertexNeighbors.get(i));
						DFS_inspector.add(vertexNeighbors.get(i));
					}
				}
			}
		}
		// We tell the user that their input cannot be null or only made of white spaces
		else{
			System.out.println("You cannot enter an empty search selection or only white spaces");
			return;
		}
	}
	
	
	/**
	 * This simple class just keeps the information about a Vertex together. 
	 * You do not need to modify this class. You only need to understand how it works.
	 */
	public static class Vertex{
		private String aUrl;
		private boolean visited;
		private ArrayList<String> aWords;
		private ArrayList<Vertex> neighbors;
		
		public Vertex(String pUrl){
			this.aUrl = pUrl;
			this.visited = false;
			this.neighbors = new ArrayList<Vertex>();
			this.aWords = new ArrayList<String>();
		}
		
		public String getURL(){
			return this.aUrl;
		}
		
		public void setVisited(){
			this.visited = true;
		}
		
		public void resetVisited(){
			this.visited = false;
		}
		
		public boolean getVisited(){
			return this.visited;
		}
			
		public void addWord(String pWord){
			this.aWords.add(pWord);
		}

		public ArrayList<String> getWords(){
			return this.aWords;
		}
		
		public ArrayList<Vertex> getNeighbors(){
			return this.neighbors;
		}
		
		public void addNeighbor(Vertex pVertex){
			this.neighbors.add(pVertex);
		}

	}

	/**
	 * This simple class just keeps the information about a Search Result. It stores
	 * the occurrences of your keyword in a specific page in the graph. You do not need to modify this class. 
	 * You only need to understand how it works.
	 */
	public class SearchResult{	
		private String aUrl;
		private int aWordCount;
		
		public SearchResult(String pUrl, int pWordCount){
			this.aUrl = pUrl;
			this.aWordCount = pWordCount;
		}
		
		public int getOccurrence(){
			return this.aWordCount;
		}
		
		public String getUrl(){
			return this.aUrl;
		}
	}
	
	/**
	 * This class enables us to use the PriorityQueue type. The PriorityQueue needs to know how to 
	 * prioritize its elements. This class instructs the PriorityQueue to compare the SearchResult 
	 * elements based on their word occurrence values.
	 * You do not need to modify this class. You only need to understand how it works.
	 */
	public class WordOccurrenceComparator implements Comparator<SearchResult>{
	    @Override
	    public int compare(SearchResult o1, SearchResult o2){
	    	int x = o1.getOccurrence();
	    	int y = o2.getOccurrence();
	    	
	        if (x > y)
	        {
	            return -1;
	        }
	        if (x < y)
	        {
	            return 1;
	        }
	        return 0;
	    }
	}
}

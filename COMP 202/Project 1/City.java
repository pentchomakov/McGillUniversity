import java.util.Random;

public class City {
	private static String[] namePrefixes = { "Tirana", "Praga", "Paris",
		"Saint-Petersbourg", "Moscow", "McGill", "Vladivostok", "LosAngeles", "Sotchi", "NewYork",
		"Zagreb", "Sofia", "Montreal", "Reykjavic", "GeorgeTown", "Beijing", "Budapest", "Belgrad",
		"Skopije", "Seattle", "Madrid", "Habana", "Quebec", "Buenos Aires", "GuatemalaCity", "Astana", 
		"Sarajevo", "Casablanca", "Tokyo", "Cairo", "Albaqurque", "SanJose" };
	
	private static String[] nameSuffixes = { "ville", "vale", "_City", "town", "ton",
			"hill", "field", "land", "ia", "furt", "grad", "lia", "stadt", "stan" };
	
	private String nameCity;
	private Vector2D pos;
	private City[] neighbours; 
	private boolean explored = false;

	public City() 
	{
	   //We get the length of every array (since they could change, we don't want to count them, 1 by 1)
	   Random randNumb = new Random();
	   String newName = namePrefixes[randNumb.nextInt(namePrefixes.length-1)] + nameSuffixes[randNumb.nextInt(nameSuffixes.length-1)];
	   this.nameCity = newName;
	   
	   //Now we make a pair of random coordinates (x,y) for every city
	   int xVal = randNumb.nextInt(151);
	   int yVal= randNumb.nextInt(151);
	   
	   //We must parse them into doubles since our methods in Vector2D are set in double
	   double i = (double)xVal;
	   double j = (double)yVal;
	   this.pos = new Vector2D(i, j);
	}
	
	
	public void setNeighbours(int maxDist, City[] cities)
	{
		int n = 0;
		Vector2D myPos = this.pos;
	
		for (int i = 0; i < cities.length; i++)
		{
			double d = myPos.distance(cities[i].pos); 
		
			//Here it is important to verify that d!=0 in order to prevent a city becoming its own neighbour.
			if (d < maxDist && d != 0)
			{
				n++;
			}
		}
		
		neighbours = new City[n];
		//Re-initialize n to 0 to restart the process.
		n = 0;
		
		//asda
		for (int i = 0; i < cities.length; i++)
		{
			double d = myPos.distance(cities[i].pos); 
			
			if (d < maxDist && d != 0)
			{
				neighbours [n] = cities[i];
				n++;
			}
	   }	
	   return;
	}
	
	//Searches to see which cities are connected to the current city.
	//If a city can be reached, its boolean 'explore' value will be true after this method is called
	//Otherwise, it will be false.
	public void explore() 
	{
		//We explore every city, to check if they are neighboors
		explored = true; 
		
		   for (int i = 0; i < neighbours.length; i++)
		   {
		     if (neighbours[i].explored == false)
		     {
		       neighbours[i].explore();
		     }
		   }
	}
	
	//We export our private variables with public methods
	public Vector2D getPos()
	{
		return pos;
	}

	public String getNameCity() {
		return nameCity;
	}
	
	public City[] getNeighbours(){
		return neighbours;
	}
	public boolean getExplored(){
		   return explored;
		 }
}


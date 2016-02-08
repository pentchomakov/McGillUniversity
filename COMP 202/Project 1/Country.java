public class Country {
	
	 //Basic private variables for this object class
	  private String nameCity;
	  private City[] cities;
	  private boolean isConnected;
	  
	  //We want to populate our City array to make it possible
	  public Country(String name, int j, int maxDist)
	  {
	    //We set the name
	    this.nameCity = name;
	    this.cities = new City[j];
	    
	    //Populate the cities array (City[]) with Cities that have random names from our City object class and random vector coordinates
	    for (int i=0 ; i<j ; i++)
	    {
	      cities[i] = new City();
	    }
	    
	    //this is to create the maximum distance between neighbours; defining what a neighboor is
	    for (int i=0 ; i<j ; i++)
	    {
	      cities[i].setNeighbours(maxDist, cities);
	    }
	  }

	  //Here is our method to check if all cities are connected within a given random country
	  public boolean setConnectivity()
	  {
	    //It is set to true by default. If we are proved wrong, we shall change it.
	    boolean isConnected = true;
	    
	    //From this point on, we will check if every city is connected to each other.
	    //We can start with the first city. If every city is connected to it, then we proceed, if not, then it is false automatically
	    cities[0].explore();
	    
	    //Then we check the other connections
	    for (int i=0 ; i<cities.length ; i++)
	    {
	      if (cities[i].getExplored() == false)
	      {
	        isConnected = false;
	      }
	    }
	    return isConnected;
	  }
	  
	  
	  //Moving our variable from private to public with those methods
	  public String getNameCity()
	  {
	    return this.nameCity;
	  }
	  
	  public City[] getCities()
	  {
	    return this.cities;
	  }
}

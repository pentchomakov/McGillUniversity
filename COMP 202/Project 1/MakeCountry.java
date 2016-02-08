public class MakeCountry {
	public static void main(String[] args){
	//We set the number of cities we want for our country
	int numCities = 15;
	//You can rename your country to whatever you like.
	String countryName = "Union of Soviet Socialist Republics";
		 
	//Max distance between each city, which we introduce in the "Country" object. The bigger the distance, the more connections we get. @150, we are assured that all cities will be connected together and connected will = true.
	Country myCountry = new Country(countryName, numCities, 50);
		  
	//We check if all the cities are connected together and state true or false.
	boolean connected = myCountry.setConnectivity();
	System.out.println(countryName + " is connected... " + connected);
		  
	//Once every thing else is complete, uncomment line below to display the image.
	CountryMap map = new CountryMap(myCountry.getCities(), myCountry.getNameCity());
	}
}

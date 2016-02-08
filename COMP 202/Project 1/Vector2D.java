import static java.lang.Math.*;
public class Vector2D 
	{
		//Basic private coordinates
		private double x;
		private double y;
  

		public Vector2D(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
		
		//Making the coordinates from private to public wit the following methods
		public double getX()
		{
			return this.x;
		}
		public double getY()
		{
			return this.y;
		}
  
	
		public String toString()
		{
			String s = ("(" + this.x + ", " + this.y + ")");
			return s;
		}
		
		//Getting the distance of the vector created between 2 cities
		public double distance(Vector2D v)
		{
			double distance = sqrt(((v.getX()-getX())*(v.getX()-this.getX())+(v.getY()-this.getY())*(v.getY()-this.getY())));
			return distance;
		}
	}

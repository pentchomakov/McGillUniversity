import java.awt.Color;
import becker.robots.City;
import becker.robots.*;

public class RobotsMoveLights {
	// Archibald Haddock (123456789)
	// COMP-202B, Section 0 (Winter 2010)
	// Instructor: Cuthbert Calculus
	// Assignment 4, Question 2

	public static void moveToDiagonal(Robot robot, int flashers) 
	{
		//First thing, in the event that there are no flashers drawn on the map, our robot has nothing to do. We return nothing
		//***ends the method***
		if(flashers == 0)
		{
			return;
		}
		//Next thing, if there are flashers, we must first pick them up.
		for(int i = 0; i<flashers;i++)
		{
			robot.move();
			robot.pickThing();
		}	
		
		//We have to come back at our original position
		robot.turnLeft();
		robot.turnLeft();
		for(int i = 0; i<flashers-1; i++)
		{
			robot.move();
		}
		
		//Now the robot has to put every flashers in diagonal
		robot.putThing();
		int layedFlashers = 1;
		for(int i = 0; i < 2; i++)
		{
		robot.turnLeft();
		}
		
		while(layedFlashers < flashers)
		{
			robot.move();
			for(int i = 0; i<3; i++)
			{
			robot.turnLeft();
			}
			robot.move();
			robot.putThing();
			robot.turnLeft();
			
			layedFlashers ++;
		}
	}
	
	public static void main(String[] args)
	{
		final int LIGHT_STREET = 4;
		final int LIGHT_AVENUE = 3;
		final int NUMBER_FLASHERS = 6;
		
		City montreal = new City(12,12);

		Robot asimo = new Robot(montreal, LIGHT_STREET, LIGHT_AVENUE - 1, Direction.EAST);

		for (int i = 0; i < NUMBER_FLASHERS; i++) 
		{
			new Flasher(montreal, LIGHT_STREET, LIGHT_AVENUE + i,true);
		}			
			
		moveToDiagonal(asimo, NUMBER_FLASHERS);		
	}

}

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse321.eventregistration.model;

// line 14 "../../../../../EventRegistration.ump"
// line 37 "../../../../../EventRegistration.ump"
public class Registration
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  private int id;

  //Registration Associations
  private Participant participant;
  private Event evnet;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Registration(Participant aParticipant, Event aEvnet)
  {
    id = nextId++;
    if (!setParticipant(aParticipant))
    {
      throw new RuntimeException("Unable to create Registration due to aParticipant");
    }
    if (!setEvnet(aEvnet))
    {
      throw new RuntimeException("Unable to create Registration due to aEvnet");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getId()
  {
    return id;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public Event getEvnet()
  {
    return evnet;
  }

  public boolean setParticipant(Participant aNewParticipant)
  {
    boolean wasSet = false;
    if (aNewParticipant != null)
    {
      participant = aNewParticipant;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setEvnet(Event aNewEvnet)
  {
    boolean wasSet = false;
    if (aNewEvnet != null)
    {
      evnet = aNewEvnet;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    participant = null;
    evnet = null;
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "participant = "+(getParticipant()!=null?Integer.toHexString(System.identityHashCode(getParticipant())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "evnet = "+(getEvnet()!=null?Integer.toHexString(System.identityHashCode(getEvnet())):"null")
     + outputString;
  }
}
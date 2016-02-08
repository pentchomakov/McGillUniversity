package ca.mcgill.ecse321.eventregistration.persistence;

import java.util.Iterator;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;

public class PersistenceEventRegistration {
	
	  private static String fileName = "eventregistration.xml";

	    private static void initializeXStream() {
	        PersistenceXStream.setFilename(fileName);
	        PersistenceXStream.setAlias("event", Event.class);
	        PersistenceXStream.setAlias("participant", Participant.class);
	        PersistenceXStream.setAlias("registration", Registration.class);
	        PersistenceXStream.setAlias("manager", RegistrationManager.class);
	    }

	    public static void setFileName(String name) {
	        fileName = name;
	    }

	public static void loadEventRegistrationModel() {
		RegistrationManager rm = RegistrationManager.getInstance();
		PersistenceEventRegistration.initializeXStream();
		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		
		if(rm2 != null) {
			
			Iterator<Participant> pIt = rm2.getParticipants().iterator();
			while(pIt.hasNext()) {
				rm.addParticipant(pIt.next());
			}
			
			Iterator<Event> eIt = rm2.getEvents().iterator();
			while(eIt.hasNext()) {
				rm.addEvent(eIt.next());
			}
			
			Iterator<Registration> rIt = rm2.getRegistrations().iterator();
			while(rIt.hasNext()) {
				rm.addRegistration(rIt.next());
			}
		}
	}
}

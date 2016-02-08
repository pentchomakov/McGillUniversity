package music;

import java.util.ArrayList;
import java.util.Hashtable;

public class MidiTrack{
    private Hashtable<Character,Integer> noteToPitch;

    private ArrayList<MidiNote> notes;
    private int instrumentId;
    
    // The constructor for this class
    public MidiTrack(int instrumentId){
        notes = new ArrayList<MidiNote>();
        this.instrumentId = instrumentId;
        this.initPitchDictionary();
    }

    // This initialises the noteToPitch dictionary,
    // which will be used by you to convert note letters
    // to pitch numbers
    public void initPitchDictionary(){
        noteToPitch  = new Hashtable<Character, Integer>();
        noteToPitch.put('C', 60);
        noteToPitch.put('D', 62);
        noteToPitch.put('E', 64);
        noteToPitch.put('F', 65);
        noteToPitch.put('G', 67);
        noteToPitch.put('A', 69);
        noteToPitch.put('B', 71);
    }

    // GETTER METHODS
    public ArrayList<MidiNote> getNotes(){
        return notes;
    }
    
    public int getInstrumentId(){
        return instrumentId;
    }
    
    // This method converts notestrings like
    // <<3E3P2E2GP2EPDP8C<8B>
    // to an ArrayList of MidiNote objects 
    // ( the notes attribute of this class )
    public void loadNoteString(String notestring){
        // convert the letters in the notestring to upper case
        notestring = notestring.toUpperCase();
        int duration = 0;
        int pitch = 0;
        int octave = 0;

        for(int i = 0; i < notestring.length(); i++){
        	char note = notestring.charAt(i);

        	//Ocatves
        	if(note =='<'){
        		octave -= 12;
        	}
        	if(note == '>'){
        		octave += 12;
        	}
        
        	//Notes
        	if(note == 'A' || note == 'B' || note == 'C' || note== 'D' || note == 'E' || note == 'F' || note == 'G'){
        		//Standard duration = 1
        		duration = 1;
        		//Getting the standard pitch of every note
        		pitch = noteToPitch.get(note);
        		//Creating a new note
        		MidiNote n = new MidiNote(pitch + octave, duration);
        		notes.add(n);
        	}
        	//Pauses
        	if(note == 'P'){
        		//Standard duration = 1
        		duration = 1;
        		//P = pause, pause = silence
        		pitch = 0;
        		//Create new note and set it to silent
        		MidiNote n = new MidiNote(pitch, duration);
           		n.setSilent(true);
        		notes.add(n);
        	}
        	//Digits which equal to duration of notes
        	if(Character.isDigit(note)){
        		//Standard duration = 1
        		duration = 1;
        		//Since it is a digit and it may have more than 1 char in length, we have to look thru the string.
        		String longerDur = "";
        		while(Character.isDigit(notestring.charAt(i))){
        			longerDur += notestring.charAt(i);
        			i++;
        		}
        		duration *= Integer.parseInt(longerDur);
        		//Create a new note with the different duration
        		MidiNote n = new MidiNote(pitch+octave, duration);
        		
        		if(notestring.charAt(i) == 'P'){
        			n.setSilent(true);
        		}
        		
        		if(notestring.charAt(i) == 'A' || notestring.charAt(i) == 'B' || notestring.charAt(i) == 'C' || notestring.charAt(i) == 'D' || notestring.charAt(i) == 'E' || notestring.charAt(i) == 'F' || notestring.charAt(i) == 'G'){
        			pitch = noteToPitch.get(notestring.charAt(i));
	       
        			n.setPitch(octave+pitch);
        			n.setDuration(Integer.parseInt(longerDur));
        		}
        		/*else if(notestring.charAt(i+1) == '<' || notestring.charAt(i+1) == '>'){
        			System.out.println("A duration cannot have a <, > or null following it");
        		}*/
        		
        		notes.add(n);
        	}
        	
        	//Adding Sharps and flats if any follow a note.
        	if(note == '#'){
        		MidiNote temp = notes.get(notes.size()-1);
        		temp.setPitch(temp.getPitch()+1);
        		notes.set(notes.size()-1, temp);
        	}
        	if(note == '!'){
        		MidiNote temp = notes.get(notes.size()-1);
        		temp.setPitch(temp.getPitch()-1);
        		notes.set(notes.size()-1, temp);
        	}
    	}
    }

    public void revert(){
        ArrayList<MidiNote> reversedTrack = new ArrayList<MidiNote>();     
        for ( int i = notes.size() - 1; i >= 0; i--){
            MidiNote oldNote = notes.get(i);
            // create a newNote
            MidiNote newNote = new MidiNote(oldNote.getPitch(), oldNote.getDuration());
            
            // check if the note was a pause
            if(oldNote.isSilent()){
                newNote.setSilent(true);
            }
             
            // add the note to the new arraylist
            reversedTrack.add(newNote);
        }
        notes = reversedTrack;
    }

    public static void main(String[] args){
    	//MidiTrack
        String notestring = "3E3P2E2GP2EPDP8C<8B>3E3P2E2GP2EPDP8C<8B>";
        int instrumentId = 0;
        MidiTrack newTrack = new MidiTrack(instrumentId);
        newTrack.loadNoteString(notestring);
        
        //New mi with beat per min.
        MusicInterpreter mi = new MusicInterpreter();
        mi.setBPM(800);
        
        //Load the track with the mi and play it
        mi.loadSingleTrack(newTrack);
        mi.play();
        
        //CLose the mi
        mi.close();
    }
}

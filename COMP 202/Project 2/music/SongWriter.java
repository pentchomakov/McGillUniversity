package music;

import java.util.*;
import java.io.*;

public class SongWriter{
    private static Hashtable<Integer,String> pitchToNote;
    
    // The constructor of this class
    public SongWriter(){
        this.initPitchToNoteDictionary();
    }
    
    // This initialises the pitchToNote dictionary,
    // which will be used by you to convert pitch numbers
    // to note letters
    public void initPitchToNoteDictionary(){
        pitchToNote  = new Hashtable<Integer, String>();
        pitchToNote.put(60, "C");
        pitchToNote.put(61, "C#");
        pitchToNote.put(62, "D");
        pitchToNote.put(63, "D#");
        pitchToNote.put(64, "E");
        pitchToNote.put(65, "F");
        pitchToNote.put(66, "F#");
        pitchToNote.put(67, "G");
        pitchToNote.put(68, "G#");
        pitchToNote.put(69, "A");
        pitchToNote.put(70, "A#");
        pitchToNote.put(71, "B");
    }

    //We take a note and transform it into notestring style.
    public String noteToString(MidiNote note){
    	//Final result
        String result = "";
        
        //Get the duration
      	int duration = note.getDuration();
      	String strDuration = Integer.toString(duration);
      	
      	//Test if it is silent;
      	boolean silent = note.isSilent();
      	//Get the pitch
      	int pitch = note.getPitch();
      	//Get Octave
      	int octave = note.getOctave();
      	//Switch pitch to regular pitch; remove added octaves
      	pitch -= 12 * octave;
      	
      	//if duration is 1, then default is 1 and we don't need to add anything to the result string
      	if(duration == 1) {
      		strDuration = "";
      	}
      	
      	//if note is silent, then it's a pause
      	if(silent == true) {
      		result = strDuration + "P";
      		return result;
      	}
      	
      	//
      	else{
      		result = strDuration + pitchToNote.get(pitch);
      	}
        return result;
    }

    //We want to convert a whole track to a notestring style
    public String trackToString(MidiTrack track){
        ArrayList<MidiNote> notes = track.getNotes();
        String result = "";
        int previous_octave = 0;
        int current_octave = 0;
        String octaves = "";
        MidiNote current_note;
        
        //Test every note, one by one
        for(int i = 0; i < notes.size(); i ++) {
        	current_note = notes.get(i);
        	current_octave = current_note.getOctave() - previous_octave;
        	previous_octave = current_note.getOctave();
        	
        	
        		if(current_octave > 0) {
        			for(int j = 0; j < current_octave; j++) {
        				octaves= octaves + ">";
        			}
        		}

        		if(current_octave < 0) {
        			current_octave = 0 - current_octave;
        			for(int j = 0; j < current_octave; j++) {
        				octaves= octaves + "<";
        			}
        		}
        	
        	//Add octaves
        	result += octaves;
        	
        	//Reset the variable for next note
        	octaves = "";
        	
        	//add the notestring and return the result
        	result += noteToString(current_note);
        }

        return result;
    }

    
    //Write to reverse
    public void writeToFile(Song s, String file_path) throws IOException, FileNotFoundException{
    
    	try{
    		
	    	//Create file writer
	    	FileWriter fw = new FileWriter(file_path);
			BufferedWriter bw = new BufferedWriter(fw);
			
			//This gets the information of the song
			String name = "" + s.getName();
	    	int bpm = 0 + s.getBPM();
	    	String beatsperminute =Integer.toString(bpm);
	    	String soundbank =s.getSoundbank();
	    	ArrayList<MidiTrack> tracks = s.getTracks();
	    	
	    	//number of lines
	    	int size = tracks.size() * 2 + 3;
	    	
	    	//Create array with the number of lines
	    	String[] lines = new String[size];
	
	    	//Put the name, bpm and soundbank path in the first three lines of the txt fiel
	    	lines[0] = "name = " + name + "_reverse";
	    	lines[1] = "bpm = " + beatsperminute;
	    	lines[2] = "soundbank = " + soundbank;
				
    			
    		for(int i = 0; i < tracks.size(); i++){
    			lines[3 + i * 2] = "instrument = " + Integer.toString(tracks.get(i).getInstrumentId()) + "\n";
    			lines[3 + i *2 + 1] = "track = " + trackToString(tracks.get(i)) + "\n";
    		}
	
    		//Write every line
	    	for(int i = 0; i < size; i++) {
		    	bw.write(lines[i]);
		    	bw.newLine();
	    	}
	    	
	    	bw.close();
	    	fw.close();
    	}
    	catch(FileNotFoundException e){
    		System.out.println(e);
    	}
    	
    	
    	
    }
    // Implement the void writeToFile( Song s1 , String file_path) method
    // This method writes the properties of the Song s1 object
    // and writes them into a file in the location specified by 
    // file_path. This file should have the same format as the sample
    // files in the 'data/' folder.

    public static void main( String[] args) throws IOException{
    	
    	
    try{
    	SongWriter writer = new SongWriter();
    	
    	String song_file_path = "src/music/data/07.txt";
    	
    	Song song = new Song();
    	song.loadFromFile(song_file_path);
    	String name = song.getName() + "_reverse.txt";
    	song.revert();
    	
    	
    	
        String destination_path = "src/music/data/" + name;

    	writer.writeToFile(song, destination_path);
    }
    catch(IOException e) {
     System.out.println("I can't file the file you're trying to revert!");
     }
   
    		
    		

    	
        // Create a Song object

        // Load text file using the given song_filename, remember to 
        // catch the appropriate Exceptions, print meaningful messages!
        // e.g. if the file was not found, print "The file FILENAME_HERE was not found"

        // call the revert method of the song object.
        
        // Create a SongWriter object here, and call its writeToFile( Song s, String file_location) method.

    }
}
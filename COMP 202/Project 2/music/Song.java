package music;
import java.io.*;
import java.util.*;

public class Song{
    String myName;
    int myBeatsPerMinute;
    String mySoundbank;
    ArrayList<MidiTrack> myTracks;
    
    // The constructor of this class
    public Song(){
        myTracks = new ArrayList<MidiTrack>();
        myBeatsPerMinute = 200;
        mySoundbank = "";
        myName = "Default_Name";
    }

    // GETTER METHODS

    public String getName(){
       return myName;
    }

    public String getSoundbank(){
       return mySoundbank;
    }
    
    public int getBPM(){
        return myBeatsPerMinute;
    }

    public ArrayList<MidiTrack> getTracks(){
        return myTracks;
    }

	public void loadFromFile(String file_path)throws IOException{
		//Initialize instrument ID
		
		int instrumentId = 0;
		String notestring = "";

		//Opening/reading file
		FileReader fr = new FileReader(file_path);
		BufferedReader br = new BufferedReader(fr);
		
		//Reading until next line is null
		String currentLine = br.readLine();
		
		//Checking next lines
		while(currentLine != null){
			/*Getting all info from the file. We do not put else if(s) 
			 * so that  if a certain element is not found but others exist, 
			 * it keeps on going on. Also, the result of splitting creates an 
			 * array of results of 2 things. We take the 2nd one which is in
			 * the index of the "virtual array" 1.
			*/
			if(currentLine.startsWith("name")){
				this.myName = currentLine.split(" = ")[1];
			} 
			if(currentLine.startsWith("bpm")){
				this.myBeatsPerMinute = Integer.parseInt(currentLine.split(" = ")[1]);
			}
			if(currentLine.startsWith("soundbank")){
				this.mySoundbank = currentLine.split(" = ")[1];
			}
			if(currentLine.startsWith("instrument")){
				instrumentId = Integer.parseInt(currentLine.split(" = ")[1]);
			}
			if(currentLine.startsWith("track")){
				notestring = currentLine.split(" = ")[1];
				//Creating new object MidiTrack... If not, we only get 1 track
				//Inside the array of tracks
				MidiTrack Track = new MidiTrack(instrumentId);
				Track.loadNoteString(notestring);
				myTracks.add(Track);
			}
			//Reads next line, while loop continues or not
			currentLine = br.readLine();
		}
		
		//Close file and buffered reader
		br.close();
		fr.close();
	}
    
    public void revert(){
        for (int i = 0; i<myTracks.size(); i++){
            myTracks.get(i).revert();
        }
    }
}
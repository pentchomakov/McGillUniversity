package music;

public class MidiNote{
    // This is a number corresponding to the pitch value
    // of this note. This follows the MIDI numbering
    // from 21 ( an A0 note ) to 108 ( a C8 note ) 
    private int pitch = 60;

    // This is the duration of this note in beats
    private int duration = 1;

    // This determines if this note gets played, or
    // if this represents a silence of a particular 
    // duration.
    private boolean silent = false;

    public MidiNote( int pitch, int duration ){
        this.setPitch(pitch);
        this.setDuration(duration);
        // minimum duration for a note is 1 beat
        if(this.duration <= 0){
            this.duration = 1;
        }
    }
    
    // GETTER METHODS

    public int getPitch(){
    	return pitch;
    }
    
    public int getDuration(){
       return duration;	
    }
    
    public boolean isSilent(){
        return silent;
    }
    
    // This method will tell you how many octaves up or down
    // this note is. A value of 0 means that the note is in the
    // range from 60 ( a C4 note ) to 71 ( a B4 note )
    public int getOctave(){
        int octave = (int) (Math.floor((this.pitch - 60.0)/12.0));
        return octave;
    }    
    
    // SETTER METHODS

    public boolean setPitch(int pitch){
    	if (pitch < 21){
    	   if (pitch == 0){
    		   this.pitch = 0;
    		   return true;
    	   }
    	   this.pitch = 21;
    	} else if (pitch <= 108) {
    		this.pitch = pitch;
    		return true;
        } else {
            this.pitch = 108;
        }
        System.out.println("Pitch "+pitch+" out of valid range [ 21 to 108 ]!");
    	return false;
    }
    
    public boolean setDuration(int duration){
    	if ( duration > 0){
    		this.duration = duration;
    		return true;
    	}
    	return false;
    }   
    
    public void setSilent(boolean value){
        this.silent = value;
    }
    
    // toString method. Useful for debugging your code
    public String toString(){
        return "( duration: "+duration+", pitch: "+pitch+", silent: "+silent+" )";
    }

    public int getVolume(){
        return 64;	
    }
}

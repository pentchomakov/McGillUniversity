package music;
import java.io.*;
import java.util.*;

public class PlaySong{
    public static void main( String[] args){
        MusicInterpreter myMusicPlayer = new MusicInterpreter();
        System.out.println(myMusicPlayer.availableInstruments());
        
        //Scanner scan = new Scanner(System.in);
        //int nbSong = scan.nextInt();
        //System.out.println("Enter a number between 1 and 9 to open tracks");
        
        String song_file_path = "src/music/data/10.txt";
        Song song = new Song();
        //Since we will be opening files and reading them, we will use try/catch
        try{
        	song.loadFromFile(song_file_path);
        }
        catch(IOException e) {
        	System.out.println(e);
        	System.out.println("Invalid file path");
        }
        
        //New music interpreter object here
        MusicInterpreter mi = new MusicInterpreter();
        //Open, play, close
        mi.loadSong(song);
        mi.play();
        mi.close();
    }
}
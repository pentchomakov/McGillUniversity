package music;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class MusicInterpreter {

    Sequencer sequencer;
    Sequence sequence;
    Synthesizer synthesizer;
    Track currentTrack;
    Instrument[] instruments;

    long startTime = System.currentTimeMillis();

    public MusicInterpreter() {
        try {
            // get a copy of the default system synthesizer
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            
            // get the current instruments
            instruments = synthesizer.getAvailableInstruments();
                        
            // Get the default system sequencer
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.setTempoInBPM(200);
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
            // set the resolution to 1 tick per quarter note
            sequence = new Sequence(Sequence.PPQ, 1);
            
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        synthesizer.close();
        sequencer.close();
    }

    public void loadSoundBank(String path) {
        try {
            File f = new File(path);
            Soundbank sb = MidiSystem.getSoundbank(f);
            if (synthesizer.isSoundbankSupported(sb)){
                // unload all instruments
                for (Instrument i: instruments){
                    synthesizer.unloadInstrument(i);
                }
                synthesizer.loadAllInstruments(sb);
                instruments = synthesizer.getLoadedInstruments();
            }
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
            System.out.println(". Loaded "+ (sb.getName())+" soundbank" );
        } catch (InvalidMidiDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MidiUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        instruments = synthesizer.getLoadedInstruments();

        System.out.println(availableInstruments());
    }

    private void selectInstrument(int instrumentId, int channel) {
        System.out.println(".. Loading "+instruments[instrumentId].getName());
        Instrument i = instruments[instrumentId];
        synthesizer.loadInstrument(i);
        int bank = i.getPatch().getProgram();
        createEvent(ShortMessage.CONTROL_CHANGE, channel, bank, 0, 0);
        createEvent(ShortMessage.CONTROL_CHANGE, channel, bank>>8, 0, 0);
        createEvent(ShortMessage.PROGRAM_CHANGE, channel, i.getPatch().getProgram(), 0, 0);
    }

    public String availableInstruments() {
        String str = "";
        int i = 0;
        for (Instrument inst : instruments) {
            str += ".. "+(i++) + " - " + inst.getName() + "\n";
        }
        return str;
    }

    public void loadTracks(ArrayList<MidiTrack> tracks) {
        int channel = 0;
        
        // delete the previous tracks
        Track[] oldTracks = sequence.getTracks();
        for( Track t: oldTracks){
            sequence.deleteTrack(t);
        }

        // add the new ones
    	for (MidiTrack midiTrack : tracks) {
    		loadSingleTrack(midiTrack, channel);
    	    channel++;
		}
    	
    	System.out.println(". Loaded "+ (channel)+" tracks" );
    }
    public void loadSingleTrack(MidiTrack midiTrack){
        loadSingleTrack(midiTrack, sequence.getTracks().length);
    }
    
    public void loadSingleTrack(MidiTrack midiTrack, int channel){
        currentTrack = sequence.createTrack();
        selectInstrument(midiTrack.getInstrumentId(), channel);
        int currentTick = 1;
        for (MidiNote note : midiTrack.getNotes()) {
            if (!note.isSilent()){
                createEvent(ShortMessage.NOTE_ON, channel, note.getPitch(), note.getVolume(), currentTick);
                createEvent(ShortMessage.NOTE_OFF, channel, note.getPitch(), note.getVolume(), currentTick + note.getDuration());
            }
            currentTick+=note.getDuration();
        }
        channel++;
        System.out.println(".. Loading track "+channel);
    }

    public void loadSong(Song song) {
        System.out.println("Loading Song: "+song.getName());
        if ( song.getSoundbank().length() > 0 ){
            // load a soundbank, if one was specified in the Song file
            loadSoundBank(song.getSoundbank());
        }
        setBPM(song.getBPM());
        loadTracks(song.getTracks());
    }
    
    public void play() {
    	System.out.println("Playing music!");
        try {
            sequencer.open();
        } catch (MidiUnavailableException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            sequencer.setSequence(sequence);
        } catch (InvalidMidiDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sequencer.start();
        // make the program wait until the sequence has been played
        long sequence_length_millis = (long)(60000*sequence.getTickLength()/sequencer.getTempoInBPM())+1;
        try {
            Thread.sleep(sequence_length_millis+ 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        sequencer.stop();
        System.out.println("Finished playing music!");
    }
    
    public void setBPM(int bpm) {
        sequencer.setTempoInBPM(bpm);
    }

    public void createEvent(int type, int chan, int num, int vel, long tick) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(type, chan, num, vel);
            MidiEvent event = new MidiEvent(message, tick);
            currentTrack.add(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

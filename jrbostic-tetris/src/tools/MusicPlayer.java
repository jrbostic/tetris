/*
 * Tetris - TCSS305 - Autumn 2013
 */

package tools;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class that implements and controls audio features.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
public class MusicPlayer {
    
    /**
     * Array of audio file paths for each level.
     */
    private static final String[] MUSIC_FILES = {"audio/music1.wav", "audio/music2.wav", 
        "audio/music3.wav", "audio/music4.wav", "audio/music5.aif", "audio/music6.wav", 
        "audio/music7.wav", "audio/music8.wav"};
    
    /**
     * The pause sound.
     */
    private final File myPause = new File("audio/gong.wav");
    
    /**
     * The intro sound.
     */
    private final File myStartClip = new File("audio/whir.wav");
    
    /**
     * The sound at level transition.
     */
    private final File myLevelUp = new File("audio/horn.wav");
    
    /**
     * The sound at game over.
     */
    private final File myGameOver = new File("audio/fail.wav");
    
    /**
     * The audio input stream.
     */
    private AudioInputStream myStream;
    
    /**
     * The current selected clip.
     */
    private Clip myClip;
    
    /**
     * The file object representing current level music.
     */
    private File myLevelSound;
    
    /**
     * The current clip path in level music array.
     */
    private int myCurrentClip;
    
    /**
     * Whether sound should be looped.
     */
    private boolean mySameLevel;
    
    /**
     * Whether sound is currently enabled.
     */
    private boolean myIsEnabled;
    
    /**
     * Constructor for music player object.
     */
    public MusicPlayer() {
        myStream = null;
        myClip = null;
        myCurrentClip = 0;
        myLevelSound = new File(MUSIC_FILES[myCurrentClip]);
        myIsEnabled = true;
        playIntro();
    }
    
    /**
     * Method for first time use of player.
     */
    public void start() {
        pause();
        mySameLevel = true;
        playCurrent();   
    }
    
    /**
     * Play game over sound.
     */
    public void gameOver() {
        myClip.stop();
        mySameLevel = false;
        playMusic(myGameOver);
    }
    
    /**
     * Set whether  player is enabled.
     * 
     * @param the_enabler true if player enabled, false otherwise
     */
    public void setEnabled(final boolean the_enabler) {
        myIsEnabled = the_enabler;
        if (!the_enabler) {
            myClip.stop();
        } 
    }
    
    /**
     * Reset player to initial state.
     */
    public void reset() {
        myClip.stop();
        myCurrentClip = 0;
        myLevelSound = new File(MUSIC_FILES[myCurrentClip]);
    }
    
    /**
     * Play the intro sound.
     */
    private void playIntro() {
        playMusic(myStartClip);
    }
    
    /**
     * Play pause sound and halt other audio.
     */
    public void pause() {
        mySameLevel = false;
        myClip.stop();
        playMusic(myPause);
    }
    
    /**
     * Resume prior audio state.
     */
    public void resume() {
        mySameLevel = true;
        myClip.stop();
        playCurrent();
    }
    
    /**
     * Plays currently selected level music.
     */
    private void playCurrent() {
        playMusic(myLevelSound);
    }
    
    /**
     * Transitions player into next level state.
     */
    public void levelUp() {
        if (myCurrentClip < MUSIC_FILES.length - 1) {
            mySameLevel = false;
            myClip.stop();
            playMusic(myLevelUp);
            myCurrentClip++;
            myLevelSound = new File(MUSIC_FILES[myCurrentClip]);
        }
        mySameLevel = true;
        playCurrent();
    }
    
    /**
     * Plays music contained in passed file.
     * 
     * @param the_file the audio file to play
     */
    private void playMusic(final File the_file) {
        if (myIsEnabled) {
            try {
                myStream = AudioSystem.getAudioInputStream(the_file);
                myClip = AudioSystem.getClip();
                myClip.open(myStream);
                if (mySameLevel) {
                    myClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                myClip.start();
            } catch (final IOException exception) {
                exception.printStackTrace();
            } catch (final UnsupportedAudioFileException exception) {
                exception.printStackTrace();
            } catch (final LineUnavailableException exception) {
                exception.printStackTrace();
            }
        }
    } 
    
}

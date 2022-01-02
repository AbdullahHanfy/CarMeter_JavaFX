/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package audioPck;

import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author mostafa
 */
public class AudioAlarm {

    File audioFile;
    Media audio;
    MediaPlayer audioPlayer;
    Button btn = new Button("Play audio");
    int Flag1 = 0;
    FlowPane pane = new FlowPane();

    public AudioAlarm() {
 audioFile = new File("//CarMeter//Speed_Alert.mp3"); // put the sound path here 
        audio = new Media(audioFile.toURI().toString());
        audioPlayer = new MediaPlayer(audio);
    }
    
    

//    public void sound_init() {
//        audioFile = new File("//CarMeter//Speed_Alert.mp3"); // put the sound path here 
//        audio = new Media(audioFile.toURI().toString());
//        audioPlayer = new MediaPlayer(audio);
//
//    }

    public void play_sound() {
        audioPlayer.play();
    }

    public void stop_sound() {
        audioPlayer.stop();
    }
}

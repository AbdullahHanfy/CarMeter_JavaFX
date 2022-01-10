/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommuniCation;

import static carmeter.CarMeter.audio;
import static carmeter.CarMeter.gps_vaild_data;
import static carmeter.CarMeter.flag_position;
import static carmeter.CarMeter.latitude;
import static carmeter.CarMeter.longitude;
import static carmeter.CarMeter.serialComm;
import static carmeter.CarMeter.speed;
import static carmeter.CarMeter.speedNode;
import java.io.IOException;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.SentenceValidator;

/**
 *
 * @author Abdullah Hanfy
 */
public class ReadLine implements Runnable {
       
        @Override
        public void run() {
            while (true) {
                try {
                  Thread.sleep(10);
                    while (serialComm.buf != null && ((serialComm.temp = serialComm.buf.readLine()) != null)) {

                        if (SentenceValidator.isValid(serialComm.temp)) {
                            //System.out.println(serialComm.temp );

                            SentenceFactory sf = SentenceFactory.getInstance();
                            //if (sf.hasParser(serialComm.temp)){
                            Sentence s = sf.createParser(serialComm.temp);

                            if ("RMC".equals(s.getSentenceId())) {
                                RMCSentence rmc = (RMCSentence) s;
                                speed = ( (rmc.getSpeed()) > 0 ) ? ( (rmc.getSpeed()) * 2 ) : (rmc.getSpeed());
                                speedNode.setSpeed(speed);
                                if (speed > 30) {
                                    audio.play_sound();
                                } else {
                                    audio.stop_sound();
                                }
                                System.out.println("RMC speed: " + rmc.getSpeed());

                            } else if ("GGA".equals(s.getSentenceId())) {
                                GGASentence gga = (GGASentence) s;
                                latitude = gga.getPosition().getLatitude();
                                longitude = gga.getPosition().getLongitude();
                                gps_vaild_data = true;
                                //System.out.println("latitude: " + latitude);
                                //System.out.println(",longitude: " + longitude);
                                System.out.println("GGA position: " + gga.getPosition());
                                flag_position = 1;
                            }
                            //}
                        }
                    }
                } catch (IOException | InterruptedException ex) {
                    //ex.printStackTrace();
                    System.out.println("please connect your mobile or make sure or if you are already connected make sure that you have gps now connected on your device");
                }
            }

        }
    }

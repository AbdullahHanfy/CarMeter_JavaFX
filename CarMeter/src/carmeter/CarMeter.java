/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmeter;

/*Importing A class That acts as a Node to represent Google Map*/
import Gaugepkg.GaugeClass;
import Map.GMap;
import SerialCommunication.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.SentenceValidator;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import net.sf.marineapi.nmea.sentence.GGASentence;
import javafx.scene.control.TextField;


/**
 *
 * @author Abdullah Hanfy
 */
public class CarMeter extends Application {

    GaugeClass gauge;
    SerialCommunication serialComm;
    int h = 800;
    Button start_button = new Button("start");
    StackPane start_pane = new StackPane();
    Button lb = new Button("welcome to our app");
    StackPane carMeter_pane = new StackPane();
    StackPane endTrip_pane = new StackPane();
    Label stop = new Label("End trip");
    boolean st = false;
    int scene_flag = 1;
    Pane speedoMeter_pane = new Pane();
    Scene start_scene;
    Scene carMeter_scene;
    Thread thread_readLine;
    Text text_latitude;
    Text text_longitude;
    Text text_speed;
    int flag_position=0;

    /*Initial paramter that will be used in Google Map*/
    public static double latitude = 30.1005;
    public static double longitude = 31.2777;
    public static double speed = 0;

    @Override
    public void init() {
        try {
            serialComm=new SerialCommunication();
            thread_readLine = new Thread(new ReadLine());
        
            //thread_readLine.start();
         
        } catch (Exception ex) {
            System.out.println("Init Exception");
            ex.printStackTrace();
        }
        gauge = new GaugeClass();
        serialComm=new SerialCommunication();
        carMeter_scene = new Scene(carMeter_pane, 1.5 * h, h);
        start_pane.getChildren().addAll(lb, start_button);
        start_scene = new Scene(start_pane);
        endTrip_pane.getChildren().add(stop);

        speedoMeter_pane.setMaxWidth(h / 3);
        speedoMeter_pane.setMaxHeight(speedoMeter_pane.getMaxWidth());
        endTrip_pane.setMaxWidth(h / 7);
        endTrip_pane.setMaxHeight(endTrip_pane.getMaxWidth());

        start_button.setTranslateY(50);
        lb.setTranslateY(-50);

        //panes 
        speedoMeter_pane.getChildren().add(gauge.setGauge());

        start_pane.setStyle("-fx-background-color: rgba(20, 21, 24, 1);");
        endTrip_pane.setStyle("-fx-background-color: rgba(255, 177, 39, 1); -fx-background-radius: 75;");
        carMeter_pane.setStyle("-fx-background-color: rgba(18, 18, 18, 1);");
        speedoMeter_pane.setStyle("-fx-background-color: rgba(0, 100, 100, 1); -fx-background-radius: 150;");
        lb.setStyle("-fx-background-color: rgba(0, 100, 100, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        start_button.setStyle("-fx-background-color: rgba(255, 177, 39, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

    }

    @Override
    public void start(Stage primaryStage) {
        
                Button button1 = new Button("Start");
        Button button2 = new Button("Stop");
        button2.setDisable(true);
      button1.setOnAction((ActionEvent event) -> {
          try {
              serialComm.connect();//contains thread
          } catch (Exception ex) {
              Logger.getLogger(CarMeter.class.getName()).log(Level.SEVERE, null, ex);
          }
          if (thread_readLine.isAlive()==false){
              thread_readLine.start();}
          else {
              thread_readLine.resume();
          }
          button1.setDisable(true);button2.setDisable(false);
                } //start button
                );
      
       button2.setOnAction(new EventHandler<ActionEvent>() {//stop button
            @Override
            public void handle(ActionEvent event) {
                serialComm.disconnect();
                if (thread_readLine.isAlive()==true){
                    thread_readLine.suspend();
                }
                button2.setDisable(true);
                button1.setDisable(false);
                text_latitude.setText("");
                text_longitude.setText("");
                text_speed.setText("");
                flag_position =0;
            }
        });
        /*Root Pane To add nodes*/
        Pane root = new Pane();

        /*Create a node of Google map as object from seperated class Called My Map*/
        GMap mp = new GMap();
        mp.createUI(carMeter_pane);
        carMeter_pane.getChildren().addAll(speedoMeter_pane, endTrip_pane);

        primaryStage.setScene(start_scene);
        start_button.setOnAction((event) -> {
            primaryStage.setScene(carMeter_scene);
        });

        endTrip_pane.setOnMouseClicked((event) -> {
            primaryStage.setScene(start_scene);

        });

        primaryStage.setHeight(h);
        primaryStage.setWidth(1.5 * h);
        st = true;

        endTrip_pane.setTranslateX(primaryStage.getWidth() / 2 - endTrip_pane.getMaxWidth() * .77);
        endTrip_pane.setTranslateY(primaryStage.getHeight() / 2 - endTrip_pane.getMaxHeight() * .77);
        speedoMeter_pane.setTranslateX(-(primaryStage.getWidth() / 2) + (speedoMeter_pane.getMaxWidth() * .77));
        speedoMeter_pane.setTranslateY(primaryStage.getHeight() / 2 - (speedoMeter_pane.getMaxHeight() * .77));
        stop.setStyle("-fx-background-color: rgba(0, 18, 18, 0.0); -fx-font-color: rgba(255, 255, 255); -fx-font:  bold 30px 'serif';");

        //primaryStage.setMaximized(false);
        //primaryStage.setm(false);
        primaryStage.setTitle("CarMeter APP");

        /*To terminate all non-GUI threads when pressing exit buttons*/
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();
    }
    class ReadLine implements Runnable 
    {
        @Override
        public void run ()
        {  
            while(true)
            {
            try {
                while(serialComm.buf != null &&((serialComm.temp = serialComm.buf.readLine()) != null)){
                    if (SentenceValidator.isValid(serialComm.temp)) {
                    //System.out.println(serialComm.temp );
                    
                    SentenceFactory sf = SentenceFactory.getInstance();
                    //if (sf.hasParser(serialComm.temp)){
                    Sentence s= sf.createParser(serialComm.temp);
                
                    if("RMC".equals(s.getSentenceId())) { 
				RMCSentence rmc= (RMCSentence) s;
                                speed =rmc.getSpeed();
                                System.out.println("RMC speed: " + rmc.getSpeed());
                                
                    }
                    else if ("GGA".equals(s.getSentenceId())) {
                            GGASentence gga = (GGASentence) s;
                            latitude=gga.getPosition().getLatitude();
                            longitude = gga.getPosition().getLongitude();
                         
                            text_latitude.setText(Double.toString( latitude));
                            text_longitude.setText(Double.toString( longitude));
                            //System.out.println("latitude: " + latitude);
                            //System.out.println(",longitude: " + longitude);
                            System.out.println("GGA position: " + gga.getPosition());
                            flag_position=1;
                    }
                    //}
                          }
                }
            } catch (Exception ex) {
                    //ex.printStackTrace();
                    System.out.println("please connect your mobile or make sure or if you are already connected make sure that you have gps now connected on your device");
                }
            }      
        
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}


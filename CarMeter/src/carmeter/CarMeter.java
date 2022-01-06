/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmeter;

import Map.GMap;
import static Map.GMap.t;
import Map.VeiwTripMap;
import audioPck.AudioAlarm;
import com.sun.javafx.application.LauncherImpl;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.ActionEvent;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.SentenceValidator;

/**
 *
 * @author asmaa
 */
public class CarMeter extends Application {

    GMap mp;
    private static final int COUNT_LIMIT = 10;
    AudioAlarm audio;
    SerialCommunication serialComm;
    String writeTrips;
    String long_start, lat_start, long_end, lat_end, time;
    double dlong_start, dlat_start, dlong_end, dlat_end;
    int counter;
    String[][] trips;
    double appHeight = 700;
    double appWidth = 1000;
    boolean started = false;
    public static boolean connected_com = false;

    boolean text_cleared = false;
    Gauge gauge;
    //declaretion of carMeter_scene and it's componants
    Scene carMeter_scene;
    StackPane carMeter_pane = new StackPane();
    StackPane speedoMeter_pane = new StackPane();
    Pane savedTrips_pane = new Pane();
    StackPane viewTrip_pane = new StackPane();
    VBox vbox = new VBox();
    Pane endTrip_pane = new Pane();
    Button start_button = new Button("start trip");
    Button back_button = new Button("back");
    Button viewTripBack_button = new Button("back");
    Button back_button1 = new Button("back");
    Button clear = new Button("Delete Trips");
    Button save_button = new Button("save trip");
    Button cancel_button = new Button("cancel trip");
    TextArea trip_name = new TextArea("Entter Your trip name HERE!");
    Button viewTrips_button = new Button("View saved trips");
    Text title = new Text("Tracks");
    //double latitude=30.0813565;double longitude=31.2383316; double speed =0;
    Thread thread_readLine;
    public static int flag_position = 0;

    Hyperlink options[] = new Hyperlink[]{
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink("")};

    /*Initial paramter that will be used in Google Map*/
    public static double latitude;
    public static double longitude;
    public static double speed;

    @Override
    public void init() throws Exception {
        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress = (double) i / 10;
            System.out.println("progress: " + progress);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(500);
        }
        audio = new AudioAlarm();

        String writeTrips = new String();
        gauge = GaugeBuilder.create().minValue(0).maxValue(220)
                .skinType(Gauge.SkinType.DIGITAL)
                .foregroundBaseColor(Color.rgb(0, 222, 249))
                .barColor(Color.rgb(0, 222, 249))
                .title("Speed")
                .unit("Km / h")
                .animated(true)
                .build();
        speedoMeter_pane.getChildren().add(gauge);
        savedTrips_pane.setMaxSize(.75 * appWidth, .75 * appHeight);
        endTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);
        viewTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);

        carMeter_pane.setStyle("-fx-background-color: rgba(230, 230, 230, 1);");
        savedTrips_pane.setStyle("-fx-background-color: rgba(195, 236, 178, 1);");
        endTrip_pane.setStyle("-fx-background-color: rgba(195, 236, 178, 1); -fx-border-color: rgba(213, 216, 219, 1);");

        viewTrip_pane.setStyle("-fx-background-color: rgba(170, 218, 255, 1);");
        speedoMeter_pane.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-background-radius: 150;");

        start_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        back_button.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        back_button1.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        clear.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        save_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        trip_name.setStyle("-fx-background-color: rgba(232, 232, 232, 1); -fx-background-radius: 7; -fx-font:  bold 15px 'serif'; -fx-font-color:  rgba(0, 0, 0, 0.3);");
        cancel_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        trip_name.setMaxSize(250, 30);
        viewTrips_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        carMeter_scene = new Scene(carMeter_pane, appWidth, appHeight);

        endTrip_pane.getChildren().addAll(back_button, trip_name, save_button, cancel_button);

        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        savedTrips_pane.getChildren().addAll(back_button1, vbox, clear);

        vbox.setTranslateY(50);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        Text title = new Text("Tracks");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        clear.setPrefWidth(200);
        for (int i = 0; i < 5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        try {

            serialComm = new SerialCommunication();

            thread_readLine = new Thread(new ReadLine());

            //thread_readLine.start();
        } catch (Exception ex) {
            System.out.println("Init Exception");
            ex.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            serialComm.connect();//contains thread
        } catch (Exception ex) {
            System.out.println("Error here");
//                    Logger.getLogger(CarMeter.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (thread_readLine.isAlive() == false) {
            thread_readLine.start();
        } else {
            thread_readLine.resume();
        }

        ///////////////// DELETE BUTTON ACTION  \\\\\\\\\\\\\\\\\\
        try {
            InetAddress ip = InetAddress.getByName("www.javatpoint.com");
            mp = new GMap();
            mp.createUI(carMeter_pane);
            carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
            clear.setOnAction(ActionEvent -> {

                try {
                    FileWriter myWriter = new FileWriter("files/saved_trips.txt");
                    myWriter.write("");
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                for (int i = 0; i < counter; i++) {
                    options[i].setText("");
                    options[i].setDisable(true);
                }
                counter = 0;
            });

            viewTrips_button.setOnAction((ActionEvent event) -> {
                System.out.println("savedtrps clicked");

                viewTrips_button.setDisable(true);
                speedoMeter_pane.setOpacity(0.5);
                start_button.setDisable(true);
                carMeter_pane.getChildren().add(savedTrips_pane);
                counter = 0;
                try {
                    File myObj = new File("files/saved_trips.txt");
                    Scanner myReader = new Scanner(myObj);

                    trips = new String[5][6];

                    while (myReader.hasNextLine() && counter < 5) {
                        String data = myReader.nextLine();
                        trips[counter] = data.split("[;]");
                        for (String a : trips[counter]) {
                            System.out.println(a);
                        }
                        counter++;
                    }

                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                for (int i = 0; i < 5; i++) {
                    options[i].setDisable(true);

                }

                for (int i = 0; i < counter; i++) {
                    options[i].setText(trips[i][0]);
                    options[i].setDisable(false);
                }

            });
            start_button.setOnAction((ActionEvent event) -> {

                if (connected_com) {
                    if (started) {

                        viewTrips_button.setDisable(true);
                        start_button.setDisable(true);
                        speedoMeter_pane.setOpacity(0.5);
                        carMeter_pane.getChildren().add(endTrip_pane);
                        start_button.setText("start trip");
                        started = false;
                        long_end = Double.toString(longitude);
                        lat_end = Double.toString(latitude);
                        start_button.setDisable(true);
                        flag_position = 0;

                    } else {
                        started = true;

                        start_button.setText("end trip");

                        long_start = Double.toString(longitude);
                        lat_start = Double.toString(latitude);
                        start_button.setDisable(true);
                        start_button.setDisable(false);

                        flag_position = 1;
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No GPS!!");
                    alert.setHeaderText("please connect GPS first to start your trip");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (!result.isPresent()) {

                    } else if (result.get() == ButtonType.OK) {

                    }
                }

            });
            back_button.setOnAction((ActionEvent event) -> {
                carMeter_pane.getChildren().clear();
                mp.createUI(carMeter_pane);
                carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);

                viewTrips_button.setDisable(false);
                start_button.setDisable(false);
                speedoMeter_pane.setOpacity(1);
                //speedoMeter_pane.setDisable(false);
            });
            back_button1.setOnAction((ActionEvent event) -> {
                carMeter_pane.getChildren().clear();
                mp.createUI(carMeter_pane);
                carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
                viewTrips_button.setDisable(false);
                start_button.setDisable(false);
                speedoMeter_pane.setOpacity(1);
            });
            viewTripBack_button.setOnAction((ActionEvent event) -> {
                viewTrip_pane.getChildren().clear();
                carMeter_pane.getChildren().clear();
                mp.createUI(carMeter_pane);
                carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button, savedTrips_pane);
                savedTrips_pane.setOpacity(1);
                vbox.setDisable(false);
                back_button1.setDisable(false);
                clear.setDisable(false);

            });

            trip_name.setOnMouseClicked((event) -> {
                if (!text_cleared) {
                    trip_name.setText("");
                    text_cleared = true;
                }
            });

            options[0].setOnAction((ActionEvent event) -> {
                System.out.println("I am in option 1");
                dlong_start = Double.parseDouble(trips[0][1]);
                dlat_start = Double.parseDouble(trips[0][2]);
                dlong_end = Double.parseDouble(trips[0][3]);
                dlat_end = Double.parseDouble(trips[0][4]);

                System.out.println(dlong_start + " " + dlat_start + " " + dlong_end + " " + dlat_end); // for trial

                VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
                
                m.createUI(viewTrip_pane);
                /*add this line in options after adding map*/
                viewTrip_pane.getChildren().add(viewTripBack_button);
                //viewTrip_pane.getChildren().add();
                carMeter_pane.getChildren().add(viewTrip_pane);
                vbox.setDisable(true);
                back_button1.setDisable(true);
                clear.setDisable(true);

            });
            options[1].setOnAction((ActionEvent event) -> {
                System.out.println("I am in option 2");
                dlong_start = Double.parseDouble(trips[1][1]);
                dlat_start = Double.parseDouble(trips[1][2]);
                dlong_end = Double.parseDouble(trips[1][3]);
                dlat_end = Double.parseDouble(trips[1][4]);
                VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
                m.createUI(viewTrip_pane);
                /*add this line in options after adding map*/
                viewTrip_pane.getChildren().add(viewTripBack_button);
                carMeter_pane.getChildren().add(viewTrip_pane);
                vbox.setDisable(true);
                back_button1.setDisable(true);
                clear.setDisable(true);

            });
            options[2].setOnAction((ActionEvent event) -> {
                
                System.out.println("I am in option 3");
                dlong_start = Double.parseDouble(trips[2][1]);
                dlat_start = Double.parseDouble(trips[2][2]);
                dlong_end = Double.parseDouble(trips[2][3]);
                dlat_end = Double.parseDouble(trips[2][4]);
                
                VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
                m.createUI(viewTrip_pane);

                /*add this line in options after adding map*/
                viewTrip_pane.getChildren().addAll(viewTripBack_button);
                carMeter_pane.getChildren().addAll(viewTrip_pane);
                vbox.setDisable(true);
                back_button1.setDisable(true);
                clear.setDisable(true);

            });
            options[3].setOnAction((ActionEvent event) -> {
                System.out.println("I am in option 4");
                dlong_start = Double.parseDouble(trips[3][1]);
                dlat_start = Double.parseDouble(trips[3][2]);
                dlong_end = Double.parseDouble(trips[3][3]);
                dlat_end = Double.parseDouble(trips[3][4]);
                VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
                m.createUI(viewTrip_pane);
                /*add this line in options after adding map*/
                viewTrip_pane.getChildren().add(viewTripBack_button);
                carMeter_pane.getChildren().add(viewTrip_pane);
                vbox.setDisable(true);
                back_button1.setDisable(true);
                clear.setDisable(true);

            });
            options[4].setOnAction((ActionEvent event) -> {
                System.out.println("I am in option 5");
                dlong_start = Double.parseDouble(trips[4][1]);
                dlat_start = Double.parseDouble(trips[4][2]);
                dlong_end = Double.parseDouble(trips[4][3]);
                dlat_end = Double.parseDouble(trips[4][4]);
                VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
                m.createUI(viewTrip_pane);
                /*add this line in options after adding map*/
                viewTrip_pane.getChildren().add(viewTripBack_button);
                carMeter_pane.getChildren().add(viewTrip_pane);
                vbox.setDisable(true);
                back_button1.setDisable(true);
                clear.setDisable(true);

            });
            save_button.setOnAction((ActionEvent event) -> {

                time = "0.0";

                if (counter <= 5) {
                    writeTrips = trip_name.getText() + ";" + lat_start + ";" + long_start + ";" + lat_end + ";" + lat_end + ";" + time + ";\n";
                }
                counter++;

                try {
                    if (counter <= 5) {
                        Files.write(Paths.get("files/saved_trips.txt"), writeTrips.getBytes(), StandardOpenOption.APPEND);
                    } else {
                        System.out.println("no room for another save");
                    }
                } catch (IOException ex) {
                    //exception handling left as an exercise for the reader
                    Logger.getLogger(CarMeter.class.getName()).log(Level.SEVERE, null, ex);

                }

                carMeter_pane.getChildren().clear();
                mp.createUI(carMeter_pane);
                carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
                viewTrips_button.setDisable(false);
                start_button.setDisable(false);
                speedoMeter_pane.setOpacity(1);
                trip_name.setText("Entter Your trip name HERE!");
                text_cleared = false;
            });

            cancel_button.setOnAction((event) -> {
                carMeter_pane.getChildren().clear();
                mp.createUI(carMeter_pane);
                carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
                viewTrips_button.setDisable(false);
                start_button.setDisable(false);
                speedoMeter_pane.setOpacity(1);
                trip_name.setText("Entter Your trip name HERE!");
                text_cleared = false;
            });
//            for (Hyperlink n : options) {
//                n.setOnAction((event) -> {
//                    carMeter_pane.getChildren().add(viewTrip_pane);
//                    vbox.setDisable(true);
//                    back_button1.setDisable(true);
//                    clear.setDisable(true);
//                });
//            }

            appHeight = carMeter_pane.getHeight();
            appWidth = carMeter_pane.getWidth();

            viewTrips_button.setTranslateX(appWidth / 2 - 150);
            viewTrips_button.setTranslateY(appHeight / 2 - 30);
            clear.setTranslateX(650);
            clear.setTranslateY(500);

            trip_name.setTranslateX(150);
            trip_name.setTranslateY(appHeight / 2 - 200);

            save_button.setTranslateX(80);
            save_button.setTranslateY(appHeight / 2 - 100);

            cancel_button.setTranslateX(320);
            cancel_button.setTranslateY(appHeight / 2 - 100);

            start_button.setTranslateX(appWidth / 2 - 150);
            start_button.setTranslateY(appHeight / 2 - 100);

            speedoMeter_pane.setMaxSize(appWidth / 4, appWidth / 4);

            speedoMeter_pane.setTranslateX(-appWidth / 2 + speedoMeter_pane.getMaxWidth() / 2);
            speedoMeter_pane.setTranslateY(carMeter_pane.getHeight() / 2 - speedoMeter_pane.getMaxHeight() / 2);

            primaryStage.setResizable(false);
            primaryStage.setTitle("CarMeter APP");
            primaryStage.setScene(carMeter_scene);
            primaryStage.setOnCloseRequest(event -> System.exit(0));
            primaryStage.show();
        } catch (UnknownHostException ex) {
            //Adding audio file here 
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Internet Connection!!");
            alert.setHeaderText("No Internet Coneection");
            alert.setContentText("Please ! Reconnect to internet and try Agian");

            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
                primaryStage.setOnCloseRequest(event -> System.exit(0));
            } else if (result.get() == ButtonType.OK) {
                primaryStage.setOnCloseRequest(event -> System.exit(0));
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(CarMeter.class, MyPreloader.class, args);
    }

    class ReadLine implements Runnable {

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
                                speed = rmc.getSpeed();
                                gauge.setValue(speed);
                                if (speed > 10) {
                                    audio.play_sound();
                                } else {
                                    audio.stop_sound();
                                }
                                System.out.println("RMC speed: " + rmc.getSpeed());

                            } else if ("GGA".equals(s.getSentenceId())) {
                                GGASentence gga = (GGASentence) s;
                                latitude = gga.getPosition().getLatitude();
                                longitude = gga.getPosition().getLongitude();
                                connected_com = true;
                                //System.out.println("latitude: " + latitude);
                                //System.out.println(",longitude: " + longitude);
                                System.out.println("GGA position: " + gga.getPosition());
                                flag_position = 1;
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

}

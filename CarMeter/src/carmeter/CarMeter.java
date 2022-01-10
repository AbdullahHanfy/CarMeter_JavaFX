package carmeter;

import CommuniCation.ReadLine;
import CommuniCation.SerialCommunication;
import Map.*;
import SpeedPackage.SpeedNode;
import audioPck.AudioAlarm;
import com.sun.javafx.application.LauncherImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Abdullah Hanfy
 * @author Asmaa Ebrahim
 * @author Mostafa AbdelRazik
 * @author Ahmed Sherif
 * @author Asmaa Saied
 */
public class CarMeter extends Application {

    GMap mp;
    private static final int COUNT_LIMIT = 10;
    public static AudioAlarm audio;
    public static SerialCommunication serialComm;
    String writeTrips;
    String long_start, lat_start, long_end, lat_end, time;
    double dlong_start, dlat_start, dlong_end, dlat_end;
    int counter;
    String[][] trips;
    double appHeight = 500;
    double appWidth = 800;
    boolean started = false;
    public static boolean gps_vaild_data = false;
    public static boolean comm_port_coneected = false;

    boolean text_cleared = false;
//    Gauge gauge;
    public static SpeedNode speedNode;
    //declaretion of carMeter_scene and it's componants
    Scene carMeter_scene;
    StackPane carMeter_pane = new StackPane();
    StackPane speedoMeter_pane = new StackPane();
    Pane savedTrips_pane = new Pane();
    StackPane viewTrip_pane = new StackPane();
    VBox vbox = new VBox();
    Pane endTrip_pane = new Pane();
    Button start_button = new Button("start trip");

    /*Adding refresh button*/
    Image internet_img;
    Image serial_img;
    ImageView internet_view;
    ImageView serial_view;
    //Creating a Button
    Button refresh_map_button;
    Button refresh_serial_button;

    Button back_button = new Button("back");
    Button viewTripBack_button = new Button("back");
    Button back_button1 = new Button("back");
    Button clear = new Button("Delete Trips");
    Button save_button = new Button("save trip");
    Button cancel_button = new Button("cancel trip");
    TextArea trip_name = new TextArea("Entter Your trip name HERE!");
    Button viewTrips_button = new Button("View saved trips");
    Text title;
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
        /*Adding refresh button*/
        internet_img = new Image("Images/refresh.png");
        serial_img = new Image("Images/refresh2.png");
        internet_view = new ImageView(internet_img);
        internet_view.setFitHeight(40);
        internet_view.setPreserveRatio(true);

        serial_view = new ImageView(serial_img);
        serial_view.setFitHeight(40);
        serial_view.setPreserveRatio(true);
        //Creating a Button
        refresh_map_button = new Button();
        refresh_map_button.setTooltip(new Tooltip("Refresh The map"));
        //Setting the size of the button
        refresh_map_button.setMaxSize(40, 40);
        //Setting a graphic to the button
        refresh_map_button.setGraphic(internet_view);
        refresh_map_button.setTranslateX(appWidth / 2 - 80);
        refresh_map_button.setTranslateY(-appHeight / 2 + 25);

        //Creating a Button
        refresh_serial_button = new Button();
        refresh_serial_button.setTooltip(new Tooltip("Refresh The GPS"));
        //Setting the size of the button
        refresh_serial_button.setMaxSize(40, 40);
        //Setting a graphic to the button
        refresh_serial_button.setGraphic(serial_view);
        refresh_serial_button.setTranslateX(appWidth / 2 - 145);
        refresh_serial_button.setTranslateY(-appHeight / 2 + 25);

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress = (double) i / 10;
            System.out.println("progress: " + progress);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(500);
        }
        audio = new AudioAlarm();
        speedNode = new SpeedNode();
        speedNode.createUI(speedoMeter_pane);
        savedTrips_pane.setMaxSize(.75 * appWidth, .75 * appHeight);
        endTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);
        viewTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);
        carMeter_pane.setId("carMeter_pane");
        savedTrips_pane.setId("savedTrips_pane");
        endTrip_pane.setId("endTrip_pane");
        viewTrip_pane.setId("viewTrip_pane");
        speedoMeter_pane.setId("speedoMeter_pane");
        start_button.setId("start_button");
        back_button.setId("back_buttons");
        back_button1.setId("back_buttons");
        clear.setId("clear");
        save_button.setId("save_button");
        trip_name.setId("trip_name");
        cancel_button.setId("cancel_button");
        trip_name.setMaxSize(250, 30);
        viewTrips_button.setId("viewTrips_button");
        viewTripBack_button.setId("viewTripBack_button");
        save_button.setDisable(true);
        carMeter_scene = new Scene(carMeter_pane, appWidth, appHeight);
        endTrip_pane.getChildren().addAll(back_button, trip_name, save_button, cancel_button);
        savedTrips_pane.getChildren().addAll(back_button1, vbox, clear);
        vbox.setTranslateY(50);
        vbox.setTranslateX(50);
        vbox.setPadding(new Insets(25));
        vbox.setSpacing(10);
        title = new Text("Your saved trips!");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 28));
        vbox.getChildren().add(title);
        clear.setPrefWidth(200);
        for (int i = 0; i < 5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        try {
            serialComm = new SerialCommunication();
            thread_readLine = new Thread(new ReadLine());
        } catch (Exception ex) {
            System.out.println("Init Exception");
        }
        counter = 0;
        try {
            File myObj = new File("files/saved_trips.txt");
            Scanner myReader = new Scanner(myObj);
            trips = new String[5][6];
            while (myReader.hasNextLine() && counter < 5) {
                String data = myReader.nextLine();
                counter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
        if(!comm_port_coneected)
        {
            //Adding audio file here 
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No port");
            alert.setHeaderText("No GPS");
            alert.setContentText("Please ! Reconnect GPS and Press refresh Button above");
            alert.showAndWait();
        }
        else
        {
            if (thread_readLine.isAlive() == false) {
                thread_readLine.start();
            } else {
                thread_readLine.resume();
            }  
        }

        ///////////////// DELETE BUTTON ACTION  \\\\\\\\\\\\\\\\\\
        try {
            InetAddress ip = InetAddress.getByName("www.javatpoint.com");
            mp = new GMap();
            mp.createUI(carMeter_pane);
        } catch (UnknownHostException ex) {
            //Adding audio file here 
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Internet Connection!!");
            alert.setHeaderText("No Internet Coneection");
            alert.setContentText("Please ! Reconnect and Press refresh Button above");

            Optional<ButtonType> result = alert.showAndWait();
//            if (!result.isPresent()) {
//                primaryStage.setOnCloseRequest(event -> System.exit(0));
//            } else if (result.get() == ButtonType.OK) {
//                primaryStage.setOnCloseRequest(event -> System.exit(0));
//            }
        }
        refresh_serial_button.setOnAction(ActionEvent -> {
//            serialComm.disconnect();
            try {
                serialComm.connect();//contains thread
            } catch (Exception ex) {
                System.out.println("Error here");
            }
            if (thread_readLine.isAlive() == false) {
                thread_readLine.start();
                System.out.println("start thread");
            } else {
                thread_readLine.resume();
                System.out.println("resume thread");
            }

        });

        refresh_map_button.setOnAction(ActionEvent -> {
            ///////////////// DELETE BUTTON ACTION  \\\\\\\\\\\\\\\\\\
            try {
                InetAddress ip = InetAddress.getByName("www.javatpoint.com");
                mp = new GMap();
                rebuild_main_scence();
            } catch (UnknownHostException ex) {
                //Adding audio file here 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Internet Connection!!");
                alert.setHeaderText("No Internet Coneection");
                alert.setContentText("Please ! Reconnect to internet and try Agian");

                Optional<ButtonType> result = alert.showAndWait();
//                if (!result.isPresent()) {
//                    primaryStage.setOnCloseRequest(event -> System.exit(0));
//                } else if (result.get() == ButtonType.OK) {
//                    primaryStage.setOnCloseRequest(event -> System.exit(0));
//                }
            }
        });
        carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button, refresh_map_button, refresh_serial_button);
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

            if (gps_vaild_data) {
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
            rebuild_main_scence();
        });
        back_button1.setOnAction((ActionEvent event) -> {
            rebuild_main_scence();
        });
        viewTripBack_button.setOnAction((ActionEvent event) -> {
            viewTrip_pane.getChildren().clear();
            carMeter_pane.getChildren().clear();
            mp.createUI(carMeter_pane);
            carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button, savedTrips_pane, refresh_map_button, refresh_serial_button);
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
        trip_name.onKeyTypedProperty().set((KeyEvent event) -> {
            if (trip_name.getText() == "") {
                save_button.setDisable(true);
            } else {
                save_button.setDisable(false);
            }
        });
        options[0].setOnAction((ActionEvent event) -> {
            show_trip(0);
        });
        options[1].setOnAction((ActionEvent event) -> {
            show_trip(1);
        });
        options[2].setOnAction((ActionEvent event) -> {
            show_trip(2);
        });
        options[3].setOnAction((ActionEvent event) -> {
            show_trip(3);
        });
        options[4].setOnAction((ActionEvent event) -> {
            show_trip(4);
        });

        save_button.setOnAction((ActionEvent event) -> {

            time = "0.0";
            if (counter == 5) {
                trip_name.setText("your memory is full, please clear it to save another trip");
                save_button.setDisable(true);

            } else {
                if ("".equals(trip_name.getText())) {
                    trip_name.setText("Enter a trip name to save your trip!");
                } else {

                    if (counter <= 5) {
                        writeTrips = trip_name.getText() + ";" + lat_start + ";" + long_start + ";" + lat_end + ";" + long_end + ";" + time + ";\n";
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

                    rebuild_main_scence();
                    trip_name.setText("Entter Your trip name HERE!");
                }
                text_cleared = false;
                save_button.setDisable(true);
            }
        });

        cancel_button.setOnAction((event) -> {
            rebuild_main_scence();
            trip_name.setText("Entter Your trip name HERE!");
            text_cleared = false;
        });

        appHeight = carMeter_pane.getHeight();
        appWidth = carMeter_pane.getWidth();

        speedoMeter_pane.setMaxSize(appWidth / 4, appWidth / 4);
        carMeter_scene.getStylesheets().add(getClass().getResource("/CSS/MainStyle.css").toString());
        primaryStage.setResizable(false);
        primaryStage.setTitle("CarMeter APP");
        primaryStage.setScene(carMeter_scene);
        primaryStage.getIcons().add(new Image("Images/ghost.PNG"));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(CarMeter.class, MyPreloader.class, args);
    }

    private void show_trip(int index) {
        dlat_start = Double.parseDouble(trips[index][1]);
        dlong_start = Double.parseDouble(trips[index][2]);
        dlat_end = Double.parseDouble(trips[index][3]);
        dlong_end = Double.parseDouble(trips[index][4]);
        VeiwTripMap m = new VeiwTripMap(dlat_start, dlong_start, dlat_end, dlong_end);
        m.createUI(viewTrip_pane);
        viewTrip_pane.getChildren().add(viewTripBack_button);
        carMeter_pane.getChildren().add(viewTrip_pane);
        vbox.setDisable(true);
        back_button1.setDisable(true);
        clear.setDisable(true);
    }

    private void rebuild_main_scence() {
        carMeter_pane.getChildren().clear();
        mp.createUI(carMeter_pane);
        carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button, refresh_map_button, refresh_serial_button);
        viewTrips_button.setDisable(false);
        start_button.setDisable(false);
        speedoMeter_pane.setOpacity(1);
    }

}

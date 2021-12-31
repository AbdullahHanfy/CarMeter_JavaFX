/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmeter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author asmaa
 */
public class CarMeter_GUI extends Application {

    double appHeight = 800;
    double appWidth = 1200;
    boolean started = false;
    boolean text_cleared = false;

    //declaretion of carMeter_scene and it's componants
    Scene carMeter_scene;
    StackPane carMeter_pane = new StackPane();
    Pane speedoMeter_pane = new Pane();
    Pane savedTrips_pane = new Pane();
    Pane viewTrip_pane = new Pane();
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

    Hyperlink options[] = new Hyperlink[]{
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink("")};

    @Override
    public void init() {

        savedTrips_pane.setMaxSize(.75 * appWidth, .75 * appHeight);
        endTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);
        viewTrip_pane.setMaxSize(.5 * appWidth, .5 * appHeight);

        carMeter_pane.setStyle("-fx-background-color: rgba(18, 18, 18, 1);");
        savedTrips_pane.setStyle("-fx-background-color: rgba(195, 236, 178, 1);");
        endTrip_pane.setStyle("-fx-background-color: rgba(195, 236, 178, 1); -fx-border-color: rgba(213, 216, 219, 1);");

        viewTrip_pane.setStyle("-fx-background-color: rgba(170, 218, 255, 1);");
        speedoMeter_pane.setStyle("-fx-background-color: rgba(232, 232, 232, 1); -fx-background-radius: 150;");

        start_button.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        back_button.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        back_button1.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        clear.setStyle("-fx-background-color: rgba(255, 242, 175, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        save_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        trip_name.setStyle("-fx-background-color: rgba(232, 232, 232, 1); -fx-background-radius: 7; -fx-font:  bold 15px 'serif'; -fx-font-color:  rgba(0, 0, 0, 0.3);");
        cancel_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        trip_name.setMaxSize(250, 30);
        viewTrips_button.setStyle("-fx-background-color: rgba(170, 218, 255, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

        carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
        carMeter_scene = new Scene(carMeter_pane, appWidth, appHeight);

        endTrip_pane.getChildren().addAll(back_button, trip_name, save_button, cancel_button);
        
        viewTrip_pane.getChildren().add(viewTripBack_button);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        for (int i = 0; i < 5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        savedTrips_pane.getChildren().addAll(back_button1, vbox, clear);

        options[0].setText("Home");
        vbox.setTranslateY(50);
    }

    @Override
    public void start(Stage primaryStage) {

        viewTrips_button.setOnAction((ActionEvent event) -> {
            System.out.println("savedtrps clicked");
            carMeter_pane.getChildren().add(savedTrips_pane);
            viewTrips_button.setDisable(true);
            speedoMeter_pane.setOpacity(0.5);
            start_button.setDisable(true);
            //carMeter_pane.getChildren().clear();
            //carMeter_pane.getChildren().remove(savedTrips_pane);
            //savedTrips_pane.setDisable(true);
        });
        start_button.setOnAction((ActionEvent event) -> {
            if (started) {

                viewTrips_button.setDisable(true);
                start_button.setDisable(true);
                speedoMeter_pane.setOpacity(0.5);
                carMeter_pane.getChildren().add(endTrip_pane);
                start_button.setText("start trip");
                started = false;
            } else {
                started = true;

                start_button.setText("end trip");
            }
        });
        back_button.setOnAction((ActionEvent event) -> {
            carMeter_pane.getChildren().clear();
            carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
            viewTrips_button.setDisable(false);
            start_button.setDisable(false);
            speedoMeter_pane.setOpacity(1);
            //speedoMeter_pane.setDisable(false);
        });
        back_button1.setOnAction((ActionEvent event) -> {
            carMeter_pane.getChildren().clear();
            carMeter_pane.getChildren().addAll(speedoMeter_pane, start_button, viewTrips_button);
            viewTrips_button.setDisable(false);
            start_button.setDisable(false);
            speedoMeter_pane.setOpacity(1);
        });
        viewTripBack_button.setOnAction((ActionEvent event) -> {
            carMeter_pane.getChildren().clear();
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
//        primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
//            System.out.println("ok");
//            appHeight = carMeter_pane.getHeight();
//            appWidth = carMeter_pane.getWidth();
//            speedoMeter_pane.setTranslateX(-appWidth / 2 + speedoMeter_pane.getMaxWidth() / 2);
//            speedoMeter_pane.setTranslateY(carMeter_pane.getHeight() / 2 - speedoMeter_pane.getMaxHeight() / 2);
//        });

//        primaryStage.addEventHandler(EventType.ROOT, eventHandler);.
//        setOnMouseClicked((event) -> {
//            appHeight = carMeter_pane.getHeight();
//            appWidth = carMeter_pane.getWidth();
//            speedoMeter_pane.setTranslateX(-appWidth / 2 + speedoMeter_pane.getMaxWidth() / 2);
//            speedoMeter_pane.setTranslateY(carMeter_pane.getHeight() / 2 - speedoMeter_pane.getMaxHeight() / 2);
//
//        });
        options[0].setOnMouseClicked((MouseEvent event) -> {
            savedTrips_pane.setOpacity(0.5);
            vbox.setDisable(true);
            back_button1.setDisable(true);
            clear.setDisable(true);
            carMeter_pane.getChildren().addAll(viewTrip_pane);
        });
//        
        //back_button.setTranslateX(50);
        appHeight = carMeter_pane.getHeight();
        appWidth = carMeter_pane.getWidth();

        viewTrips_button.setTranslateX(-appWidth / 2 + 150);
        viewTrips_button.setTranslateY(-appHeight / 2 + 30);
        clear.setTranslateX(650);
        clear.setTranslateY(500);

        trip_name.setTranslateX(150);
        trip_name.setTranslateY(appHeight / 2 - 200);

        save_button.setTranslateX(80);
        save_button.setTranslateY(appHeight / 2 - 100);

        cancel_button.setTranslateX(320);
        cancel_button.setTranslateY(appHeight / 2 - 100);

        start_button.setTranslateX(appWidth / 2 - 100);
        start_button.setTranslateY(appHeight / 2 - 50);

        speedoMeter_pane.setMaxSize(appWidth / 4, appWidth / 4);
        
        speedoMeter_pane.setTranslateX(-appWidth / 2 + speedoMeter_pane.getMaxWidth() / 2);
        speedoMeter_pane.setTranslateY(carMeter_pane.getHeight() / 2 - speedoMeter_pane.getMaxHeight() / 2);

        primaryStage.setResizable(false);
        primaryStage.setTitle("CarMeter APP");
        primaryStage.setScene(carMeter_scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

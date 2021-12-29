/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.Version;

/**
 *
 * @author asmaa
 */
public class gui extends Application {

    //Label l = new Label("speedometer");
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

    @Override
    public void init() {
        carMeter_scene = new Scene(carMeter_pane, 1.5 * h, h);
        start_pane.getChildren().addAll(lb, start_button);
        start_scene = new Scene(start_pane);
        endTrip_pane.getChildren().add(stop);
        carMeter_pane.getChildren().addAll(speedoMeter_pane, endTrip_pane);
        speedoMeter_pane.setMaxWidth(h / 3);
        speedoMeter_pane.setMaxHeight(speedoMeter_pane.getMaxWidth());
        endTrip_pane.setMaxWidth(h / 6);
        endTrip_pane.setMaxHeight(endTrip_pane.getMaxWidth());

        start_button.setTranslateY(50);
        lb.setTranslateY(-50);

        //panes style
        start_pane.setStyle("-fx-background-color: rgba(20, 21, 24, 1);");
        endTrip_pane.setStyle("-fx-background-color: rgba(255, 177, 39, 1); -fx-background-radius: 75;");
        carMeter_pane.setStyle("-fx-background-color: rgba(18, 18, 18, 1);");
        speedoMeter_pane.setStyle("-fx-background-color: rgba(0, 100, 100, 1); -fx-background-radius: 150;");
        lb.setStyle("-fx-background-color: rgba(0, 100, 100, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");
        start_button.setStyle("-fx-background-color: rgba(255, 177, 39, 1); -fx-background-radius: 7; -fx-font:  bold 30px 'serif';");

    }

    @Override
    public void start(Stage primaryStage) {

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
        primaryStage.setTitle("Hello map!");

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

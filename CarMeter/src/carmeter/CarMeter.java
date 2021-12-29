/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmeter;

/*Importing A class That acts as a Node to represent Google Map*/
import Map.GMap;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Abdullah Hanfy
 */
public class CarMeter extends Application {

    /*Initial paramter that will be used in Google Map*/
    public static double latitude = 30.1005;
    public static double longitude = 31.2777;
    public static double speed = 0;

    @Override
    public void start(Stage primaryStage) {
        /*Root Pane To add nodes*/
        Pane root = new Pane();
        
        /*Create a node of Google map as object from seperated class Called My Map*/
        GMap mp = new GMap();
        mp.createUI(root);
        
        
        Scene scene = new Scene(root);

        primaryStage.setTitle("CarMeter");

        primaryStage.setScene(scene);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

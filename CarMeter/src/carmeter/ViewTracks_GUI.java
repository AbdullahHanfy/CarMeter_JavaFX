/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tracks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mostafa
 */
public class ViewTracks_GUI extends Application {

    int counter;
    String[][] trips;
    Button Delete_Button;
    FlowPane Delete_pane;
    BorderPane border;
    VBox vbox;
    Hyperlink options[] = new Hyperlink[]{
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink(""),
        new Hyperlink("")};

    @Override
    public void init() {
        Delete_Button = new Button();
        Delete_pane = new FlowPane();
        border = new BorderPane();
        vbox = new VBox();
        Delete_Button.setText("Delete All");

    }

    @Override
    public void start(Stage primaryStage) {

        counter = 0;
        try {
            File myObj = new File("C:\\Users\\WINDOWS\\Desktop\\ReadTrips\\sherif.txt");
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

        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        border.setLeft(vbox);
        Text title = new Text("Tracks");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
       
        for (int i = 0; i < 5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        for (int i = 0; i < 5; i++) {
            options[i].setDisable(true);
        }

        for (int i = 0; i < counter; i++) {
            options[i].setText(trips[i][0]);
            options[i].setDisable(false);
        }
        Delete_Button.setPrefWidth(200);
        ///////////////// DELETE BUTTON ACTION  \\\\\\\\\\\\\\\\\\
        Delete_Button.setOnAction(ActionEvent -> {

            try {
                FileWriter myWriter = new FileWriter("C:\\Users\\WINDOWS\\Desktop\\ReadTrips\\sherif.txt");
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
        });

        Delete_pane.getChildren().addAll(Delete_Button);

        border.setBottom(Delete_pane);
        Scene Trips_scene = new Scene(border, 400, 400);

        primaryStage.setTitle("Saved Trackes");
        primaryStage.setScene(Trips_scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

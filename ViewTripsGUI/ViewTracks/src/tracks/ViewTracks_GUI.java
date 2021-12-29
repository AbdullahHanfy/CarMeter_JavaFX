/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tracks;

import javafx.application.Application;
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

    Button       Delete_Button;
    FlowPane     Delete_pane;
    BorderPane   border;
    VBox         vbox;

    @Override
    public void init() {
        Delete_Button    = new Button();
        Delete_pane      = new FlowPane();
        border           = new BorderPane();
        vbox             = new VBox();

        Delete_Button.setText("Delete All");

    }

    @Override
    public void start(Stage primaryStage) {

        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        border.setLeft(vbox);
        Text title = new Text("Tracks");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[]{
            new Hyperlink(""),
            new Hyperlink(""),
            new Hyperlink(""),
            new Hyperlink(""),
            new Hyperlink("")};

        for (int i = 0; i < 5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        options[0].setText("Home");
        /////////// To Hide the HyperLinks\\\\\\\\
        // Use setDisbale( false) to make it up again
        for (int i = 1; i < 5; i++) {
            options[i].setDisable(true);
        }
        // setting the width of delete button
        Delete_Button.setPrefWidth(200);
        ///////////////// DELETE BUTTON ACTION  \\\\\\\\\\\\\\\\\\
        Delete_Button.setOnAction(ActionEvent -> {

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

package service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateLABox {
    public static void display() {
        // New stage init
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
        window.setTitle("Create LA");

        // New elements init
        Button goLogIn = new Button("Ok");
        goLogIn.setMinWidth(100);
        goLogIn.setOnAction(e-> window.close());
        Label message = new Label("Window to define LA - nothing here yet");

        //Layout
        VBox layout = new VBox(25);
        layout.getChildren().addAll(message, goLogIn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 25, 25, 25));

        //Scene
        Scene scene = new Scene(layout);

        //Start
        window.setScene(scene);
        window.show();
    }
}

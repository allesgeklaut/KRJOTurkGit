package service;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    public static void display(String title, String message) {
        // New stage init
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
        window.setTitle(title);

        // New elements init
        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);
        Button closeButton = new Button("Continue");
        closeButton.setMinWidth(100);
        closeButton.setOnAction(e-> window.close());

        //Layout
        VBox layout = new VBox(25);
        layout.setPadding(new Insets(25, 25, 25, 25));
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        //Scene
        Scene scene = new Scene(layout);

        //Start
        window.setScene(scene);
        window.show();
    }
}

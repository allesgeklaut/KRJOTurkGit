package service;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Controllers.Controller;


public class AreYouSureBox extends Controller{
    private LearningUnit LU;
    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void display(LearningUnit LU) {
        this.LU = LU;
        // New stage init
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
        window.setTitle("Confirm");

        // New elements init
        Label label = new Label("Are you Sure?");
        label.setAlignment(Pos.CENTER);
        Button closeButton = new Button("Yes");
        closeButton.setMinWidth(100);
        closeButton.setOnAction(this::yesButtonClick);

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

    public void yesButtonClick(ActionEvent event) {
        try{
            Connection conn = systemData.getInstance().getDBConnection();

            String query = "delete from learning_units where id = ?";
            PreparedStatement prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, Integer.toString(LU.getId()));
            prepStmt.executeUpdate();

            if (LU instanceof LuText)
                query = "delete from lu_text_text where id = ?";
            if (LU instanceof LuFigureText)
                query = "delete from lu_figure_text where id = ?";
            if (LU instanceof LuFigureFigure)
                query = "delete from lu_figure_figure where id = ?";

            prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, Integer.toString(LU.getId()));
            prepStmt.executeUpdate();

            // reinitialize systemData instance update its members
            systemData.getInstance().reInit();

            // close settings screen and go back to login
            ( (Stage)((Node)event.getSource()).getScene().getWindow() ).close();
            newScene(parentStage,"home.fxml");

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}


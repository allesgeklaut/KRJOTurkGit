package service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordBox {
    public static void display() {
        // New stage init
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
        window.setTitle("Reset password");

        // New elements init
        Label helpMessage = new Label("Please enter you email address!");
        TextField loginEmailField = new TextField();
        loginEmailField.setPromptText("Your email address");
        loginEmailField.setMinWidth(350);
        loginEmailField.setMinHeight(27);
        loginEmailField.setAlignment(Pos.BASELINE_LEFT);
        Button resetPasswordButton = new Button("Reset password");
        resetPasswordButton.setMinWidth(100);
        resetPasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String inputText = loginEmailField.getText();

                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(inputText);
                boolean containsWhiteSpace = matcher.find();

                if(inputText.equals("")){
                    helpMessage.setTextFill(Color.web("#e60000"));
                    helpMessage.setText("Please enter your email address!");
                }
                else if(containsWhiteSpace){
                    helpMessage.setTextFill(Color.web("#e60000"));
                    helpMessage.setText("Your mail address should not contains white spaces");
                }
                else if (systemData.getInstance().isEmailExist(inputText)){
                    try{
                        Connection conn = systemData.getInstance().getDBConnection();
                        String query = "update login_data as ld inner join users as u on ld.user_id = u.user_id set ld.password = ? where u.email = ?";
                        PreparedStatement prepStmt = conn.prepareStatement(query);
                        prepStmt.setString(1, "1kL3RRrW");
                        prepStmt.setString(2, inputText);

                        prepStmt.executeUpdate();

                        MailService sendMail = new MailService();
                        sendMail.sendmessage(inputText);

                        helpMessage.setTextFill(Color.web("#33cc33"));
                        helpMessage.setText("We sent you an email with your temporary password.");
                        systemData.getInstance().reInit();

                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                else {
                    helpMessage.setTextFill(Color.web("#e60000"));
                    helpMessage.setText("Your email address is incorrect!");
                }


            }
        });

        Button closeButton = new Button("Close");
        closeButton.setMinWidth(100);
        closeButton.setOnAction(e-> window.close());

        //Layout
        VBox layout = new VBox(25);
        layout.getChildren().addAll(helpMessage, loginEmailField, resetPasswordButton, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 25, 25, 25));

        //Scene
        Scene scene = new Scene(layout);

        //Start
        window.setScene(scene);
        window.show();
    }
}

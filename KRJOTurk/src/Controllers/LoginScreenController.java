package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AlertBox;
import service.ResetPasswordBox;
import service.currentUser;
import service.systemData;
import java.io.IOException;


/**
 * Created by a1 on 25.10.17.
 */

public class LoginScreenController {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Hyperlink restPasswordLink;
    @FXML
    Button registerButton;
    @FXML
    Button signInButton;

    Parent root;


    public void loginButtonClicked(ActionEvent event) {
        if(systemData.getInstance().isLoginSuccessful(usernameField.getText(), passwordField.getText())) {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                root = FXMLLoader.load(getClass().getResource("/resources/view/home.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            window.setTitle("Home");
            window.setScene(new Scene(root, 800, 600));
        }
        else {
            AlertBox.display("Login failed", "Your password or login is wrong");
        }
    }

    public void resetPasswordOnAction() {
        ResetPasswordBox.display();
    }


    public void passwordFiledOnAction(ActionEvent event) {
        loginButtonClicked(event);
    }

    public void registerButtonClicked(ActionEvent event ) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/view/registration.fxml"));
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        window.setScene(new Scene(root, 800, 600));

    }

    @FXML
    private void initialize() {

    }
}

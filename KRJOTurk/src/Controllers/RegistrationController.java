package Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import service.LearningApplication;
import service.LearningCategory;
import service.User;
import service.systemData;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by a1 on 27.10.17.
 */
public class RegistrationController {
    @FXML
    TextFlow errorLogTextFlow;
    @FXML
    Button goBackButton;
    @FXML
    Button confirmButton;
    @FXML
    TextField nameTextField;
    @FXML
    TextField surnameTextField;
    @FXML
    TextField username;
    @FXML
    DatePicker dateOfBirthField;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    ChoiceBox genderField;
    @FXML
    PasswordField confirmPasswordField;
    @FXML
    TextFlow creatorText;
    @FXML
    CheckBox creatorCheckBox;

    Parent root;
    ObservableList list;

    public void goBackButtonPressed(ActionEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/view/login.fxml"));
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        window.setTitle("OADTurk");
        window.setScene(new Scene(root, 800, 600));
    }

    public void confirmButtonPressed(ActionEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(checkInputData()) {
            User user = new User(systemData.getInstance().getLastUserId()+1, nameTextField.getText(),
                    surnameTextField.getText(), emailTextField.getText(), Date.valueOf(dateOfBirthField.getValue()));
            user.setCreator(creatorCheckBox.isSelected());
            systemData.getInstance().addUser(user, username.getText(), passwordField.getText());
            systemData.getInstance().setCurrentUserID(user.getUser_id());
            systemData.getInstance().setCurrentUser();
            try {
                root = FXMLLoader.load(getClass().getResource("/resources/view/home.fxml"));
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            window.setTitle("Home");
            window.setScene(new Scene(root, 800, 600));

            // KRJO added these for instant delete account functionality
            systemData.getInstance().reInit();
            systemData.getInstance().setCurrentUserID(systemData.getInstance().getLastUserId());
        }
    }

    @FXML
    private void initialize() {
        setCreatorText();
        list = errorLogTextFlow.getChildren();
        Text inputText = new Text("All fields - marked with \'*\' must to be completed");
        list.add(inputText);
        initializeGender();
    }


    private void initializeGender() {
        genderField.getItems().addAll("Male", "Female");
        genderField.setValue("Female");

    }

    private boolean checkInputData () {
        boolean returnFlag = true;
        ObservableList list = errorLogTextFlow.getChildren();
        list.clear();
        if(nameTextField.getText().equals("")) {
            returnFlag = false;
            list.add(new Text("Please write your name\n"));
        }
        if(surnameTextField.getText().equals("")) {
            returnFlag = false;
            list.add(new Text("Please write your surname\n"));
        }
        if(passwordField.getText().equals("")) {
            returnFlag = false;
            list.add(new Text("Please write your password\n"));
        }
        if(username.getText().equals("")) {
            returnFlag = false;
            list.add(new Text("Please write your username\n"));
        }
        if(emailTextField.getText().equals("")) {
            returnFlag = false;
            list.add(new Text("Please write your email\n"));
        }
        System.out.println(passwordField.getText() + confirmPasswordField.getText());
        if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            returnFlag = false;
            list.add(new Text("Passwords are not equal\n"));
        }
        if(systemData.getInstance().isUsernameExist(username.getText())) {
            returnFlag = false;
            list.add(new Text("This username is already exist. Choose another one\n"));
        }
        if(systemData.getInstance().isEmailExist(emailTextField.getText())) {
            returnFlag = false;
            list.add(new Text("This email is already exist. Choose another one\n"));
        }
        return returnFlag;
    }

    private void setCreatorText() {
        Text text = new Text("Hello and welcome to our OADTurk Application! \n" +
                "You have a good opportunity to be a part of our team, and not just improve your skills " +
                "and knowledge, but also help us to create new exams and approving the Learning Units from our users." +
                "It will not take a lot of your free time, but if everybody will bring some effort, our application" +
                "would have a lot of different topics and data to learn. \nIf you want to be a part of our team, just " +
                "make a request for a new Learning Application or become a co-creator of an existing one!");
        text.setFont(Font.font("System", FontPosture.REGULAR, 18));
        creatorText.getChildren().add(text);
    }

}

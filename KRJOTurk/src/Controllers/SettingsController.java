package Controllers;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import service.LearningInstance;
import org.apache.commons.validator.routines.EmailValidator;

public class SettingsController extends Controller {

    private int UserID = 0;
    Stage parentStage;

    @FXML
    Label messageLabel;
    @FXML
    Button changeMailButton;
    @FXML
    Button changeUserNameButton;
    @FXML
    Button changePasswordButton;
    @FXML
    Button yesButton;
    @FXML
    Button noButton;
    @FXML
    TextField mailTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField oldPasswordTextField;
    @FXML
    TextField newPasswordTextField1;
    @FXML
    TextField newPasswordTextField2;
    @FXML
    Hyperlink deleteAccountLink;
    @FXML
    ComboBox<String> choseUser;


    //TODO JO: maybe registration problem when not everything is set

    @FXML
    private void initialize() {
        if (!currentUser.getInstance().isAdmin()){
            choseUser.setVisible(false);
        }
        else{
            initComboBox();
        }
        if (UserID == 0)
            UserID = systemData.getInstance().getCurrentUserID();

        mailTextField.clear();
        nameTextField.clear();
        oldPasswordTextField.clear();
        newPasswordTextField1.clear();
        newPasswordTextField2.clear();

        yesButton.setVisible(false);
        yesButton.setManaged(false);
        noButton.setVisible(false);
        noButton.setManaged(false);
    }

    /** checks if item is already used in data base
     *
     * @param newItem new item from user input (user name, email, ...)
     * @param DBTable table in data base to compare
     * @param DBColumn column in that table
     */
    private boolean alreadyInUseCheck(String newItem, String DBTable, String DBColumn){
        try{
            Connection conn = systemData.getInstance().getDBConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT "+DBColumn+" FROM "+DBTable);

            while (resultSet.next()){
                if (newItem.equals(resultSet.getString(DBColumn))){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // button functions
    public void changeMailButtonClick(ActionEvent event) {
        adminSettings();
        String inputText = mailTextField.getText();
        String DBEmail = systemData.getInstance().getDBData(UserID, "users", "email");
        EmailValidator validator = EmailValidator.getInstance();

        if (!validator.isValid(inputText)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered email address is not valid");
        }
        else if (inputText.equals(DBEmail)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered and current email address are the same");
        }
        else if (alreadyInUseCheck(inputText, "users", "email")){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered email address is already in use");
        }
        else{

            try{
                Connection conn = systemData.getInstance().getDBConnection();
                String query = "update users set email = ? where user_id = ?";
                PreparedStatement prepStmt = conn.prepareStatement(query);
                prepStmt.setString(1, inputText);
                prepStmt.setString(2, Integer.toString(UserID));

                prepStmt.executeUpdate();

                messageLabel.setTextFill(Color.web("#33cc33"));
                messageLabel.setText("Email changed");
                // reinitialize systemData instance update its members
                systemData.getInstance().reInit();

            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        initialize();
    }

    public void changeUserNameButtonClick(ActionEvent event) {
        adminSettings();
        String inputText = nameTextField.getText();
        String DBUsername = systemData.getInstance().getDBData(UserID, "login_data", "username");

        // check for whitespace
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(inputText);
        boolean containsWhiteSpace = matcher.find();

        if (inputText.equals(DBUsername)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered and current user name are the same");
        }
        else if(alreadyInUseCheck(inputText, "login_data", "username")){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered user name is already in use");
        }
        else if(inputText.equals("")){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("User field is empty");
        }
        else if(containsWhiteSpace){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("User name should not contains white spaces");
        }
        else{

            try{
                Connection conn = systemData.getInstance().getDBConnection();
                String query = "update login_data set username = ? where user_id = ?";
                PreparedStatement prepStmt = conn.prepareStatement(query);
                prepStmt.setString(1, inputText);
                prepStmt.setString(2, Integer.toString(UserID));

                prepStmt.executeUpdate();

                messageLabel.setTextFill(Color.web("#33cc33"));
                messageLabel.setText("User name changed");
                // reinitialize systemData instance update its members
                systemData.getInstance().reInit();

            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        initialize();
    }

    public void ChangePasswordButtonClick(ActionEvent event) {
        adminSettings();
        String inputText1 = oldPasswordTextField.getText();
        String inputText2 = newPasswordTextField1.getText();
        String inputText3 = newPasswordTextField2.getText();
        String DBPswd= systemData.getInstance().getDBData(UserID, "login_data", "password");

        if (!inputText1.equals(DBPswd)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("Old password is not correct");
        }
        else if (inputText2.equals(DBPswd)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The entered and current password are the same");
        }
        else if (!inputText2.equals(inputText3)){
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("The new passwords do not match");
        }
        else{

            try{
                Connection conn = systemData.getInstance().getDBConnection();
                String query = "update login_data set password = ? where user_id = ?";
                PreparedStatement prepStmt = conn.prepareStatement(query);
                prepStmt.setString(1, inputText2);
                prepStmt.setString(2, Integer.toString(UserID));

                prepStmt.executeUpdate();

                messageLabel.setTextFill(Color.web("#33cc33"));
                messageLabel.setText("Password changed");
                // reinitialize systemData instance update its members
                systemData.getInstance().reInit();

            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        initialize();
    }

    public void deleteAccountLinkClick(ActionEvent event) {
        adminSettings();
        yesButton.setVisible(true);
        yesButton.setManaged(true);
        noButton.setVisible(true);
        noButton.setManaged(true);

        messageLabel.setTextFill(Color.web("#0033cc"));
        messageLabel.setText("Delete account permanently?");
    }

    public void yesButtonClick(ActionEvent event) {
        try{
            Connection conn = systemData.getInstance().getDBConnection();

            String query = "delete from login_data where user_id = ?";
            PreparedStatement prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, Integer.toString(UserID));
            prepStmt.executeUpdate();

            query = "delete from users where user_id = ?";
            prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, Integer.toString(UserID));
            prepStmt.executeUpdate();

            // reinitialize systemData instance update its members
            systemData.getInstance().reInit();
            DeleteAccountBox.display();

            // close settings screen and go back to login
             ( (Stage)((Node)event.getSource()).getScene().getWindow() ).close();
             if (!currentUser.getInstance().isAdmin() || currentUser.getInstance().getUser_id() == UserID)
                newScene(parentStage,"login.fxml");

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void noButtonClick(ActionEvent event) {
        messageLabel.setText("");

        yesButton.setVisible(false);
        yesButton.setManaged(false);
        noButton.setVisible(false);
        noButton.setManaged(false);
    }

    public void changePreferencesButtonClick(ActionEvent event) {
        //TODO JO: implement change preferences
    }

    private void initComboBox(){
        choseUser.getItems().clear();
        for (User user: systemData.getInstance().getUsers()) {
            choseUser.getItems().add(" User ID: " + user.getUser_id() + "; " + user.getUser_name());
        }
    }

    private void adminSettings(){
        if (currentUser.getInstance().isAdmin()){
            UserID = Integer.parseInt(parseUserChoice(choseUser.getValue()));
        }
    }

    private String  parseUserChoice(String in){
        if (in != null){
            String[] tokens = in.split(": ");
            String token1 = tokens[1];
            tokens = token1.split(";");
            return tokens[0];
        }
        return Integer.toString(currentUser.getInstance().getUser_id());
    }
}

package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import service.systemData;
import service.currentUser;

public class userAddLa {

    @FXML
    Button confirmButton;
    @FXML
    TextField nameId;
    @FXML
    TextArea descriptionId;
    @FXML
    Label messageLabel;

    @FXML
    private void initialize() {

    }

    public void confirmButtonPressed(ActionEvent event) {
        String name = nameId.getText();
        String description = descriptionId.getText();
        if(name.equals("")) {
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("No name detected. Please write the name for your application");
        } else if(description.equals("")) {
            messageLabel.setTextFill(Color.web("#e60000"));
            messageLabel.setText("No description detected. Please write description for your application");
        } else {
            systemData.getInstance().addLA(name, description, 0, currentUser.getInstance().getUser_id());
            nameId.setText("");
            descriptionId.setText("");
            TreeController.getInstance().updateTree();
            TreeController.getInstance().treeInitializer();
            messageLabel.setTextFill(Color.web("#0033cc"));
            messageLabel.setText("Your Learning Application is pending for approval.");
        }
    }
}

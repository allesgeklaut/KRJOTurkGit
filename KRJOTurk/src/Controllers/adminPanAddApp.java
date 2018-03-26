package Controllers;

import javafx.fxml.FXML;
import service.AlertBox;
import service.systemData;
import service.currentUser;

public class adminPanAddApp extends adminPanelController {

    @FXML
    private void initialize() {
        addAppButton.setDisable(true);
        setNoEffects();
    }

    public void confirmButtonPressed() {
        String name = nameId.getText();
        String description = descriptionId.getText();
        if(name.equals("")) {
            AlertBox.display("No name detected", "Please write the name for your application");
        } else if(description.equals("")) {
            AlertBox.display("No description detected", "Please write description for your application");
        } else {
            systemData.getInstance().addLA(name, description, 1, currentUser.getInstance().getUser_id());
            nameId.setText("");
            descriptionId.setText("");
            TreeController.getInstance().updateTree();
            TreeController.getInstance().treeInitializer();
        }
    }
}

package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class adminPanelController extends Controller{
    @FXML
    Button addAppButton;
    @FXML
    Button editAppButton;
    @FXML
    Button addCategoryButton;
    @FXML
    Button editCategoryButton;
    @FXML
    Button confirmButton;
    @FXML
    TextField nameId;
    @FXML
    TextArea descriptionId;


    public void addAppClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/adminViews/adminPanAddApp.fxml");
    }

    public void editAppClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/adminViews/adminPanEditApp.fxml");
        editAppButton.setDisable(true);
    }

    public void addCategoryClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/adminViews/adminPanAddCategory.fxml");
        addCategoryButton.setDisable(true);
    }

    public void editCategoryClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/adminViews/adminPanEditCategory.fxml");
        editCategoryButton.setDisable(true);
    }

    void setNoEffects() {
//        addAppButton.setStyle("-fx-focus-color: transparent;");
//        editAppButton.setStyle("-fx-focus-color: transparent;");
//        addCategoryButton.setStyle("-fx-focus-color: transparent;");
//        editCategoryButton.setStyle("-fx-focus-color: transparent;");
    }
}

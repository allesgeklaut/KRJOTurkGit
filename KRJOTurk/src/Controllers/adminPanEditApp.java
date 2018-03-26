package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.AlertBox;
import service.LearningApplication;
import service.currentUser;
import service.systemData;

import java.util.ArrayList;

public class adminPanEditApp extends adminPanelController {

    @FXML
    ChoiceBox<LearningApplication> choiceBoxId;
    @FXML
    TextArea descriptionId;
    @FXML
    Button deleteButton;
    @FXML
    Button  confirmButton;
    @FXML
    TextField nameId;

    private String currentLAName;
    private ArrayList<LearningApplication> LAs = systemData.getInstance().getDataLA();

    @FXML
    private void initialize() {
        editAppButton.setDisable(true);
        initChoiceBox();
        setNoEffects();

        if (currentUser.getInstance().isCreator()){
            addAppButton.setVisible(false);
        }
    }

    public void saveButtonPressed() {
        if(nameId.getText().equals("")) {
            AlertBox.display("Add name", "Please add the name to this Application");
            return;
        }

        if(descriptionId.getText().equals("")) {
            AlertBox.display("Add description", "Please add the description to this Application");
            return;
        }

        if(systemData.getInstance().getLaByName(nameId.getText()) != null &&
                !systemData.getInstance().getLaByName(nameId.getText()).getName().equals(currentLAName)) {
            AlertBox.display("LA already exists",
                    "Learning Application with this name is already exist. Please, choose another name" +
                            " or take the old one.");
        } else {
            LearningApplication la = systemData.getInstance().getLaByName(currentLAName);
            systemData.getInstance().updateLA(nameId.getText(), descriptionId.getText(), la.getId());
            la.setName(nameId.getText());
            la.setDescription(descriptionId.getText());
            AlertBox.display("Success", "Changes was successfully added.");
            if(!nameId.getText().equals(currentLAName)) {
                TreeController.getInstance().updateTree();
                TreeController.getInstance().treeInitializer();
            }
        }
    }

    public void deleteButtonPressed() {
        systemData.getInstance().removeLAByName(currentLAName);
        AlertBox.display("Deleted", "This LA was successfully deleted.");
        nameId.setText(LAs.get(0).getName());
        descriptionId.setText(LAs.get(0).getDescription());
        TreeController.getInstance().updateTree();
        TreeController.getInstance().treeInitializer();
    }

    private void initChoiceBox() {
        for(LearningApplication la : LAs) {
            if (currentUser.getInstance().isCreator() && (la.getCreatedBy() != currentUser.getInstance().getUser_id()))
                continue;
            choiceBoxId.getItems().add(la);
        }
        choiceBoxId.setValue(LAs.get(0));
        currentLAName = LAs.get(0).getName();
        nameId.setText(LAs.get(0).getName());
        descriptionId.setText(LAs.get(0).getDescription());
        ChangeListener<LearningApplication> changeListener = new ChangeListener<LearningApplication>() {
            @Override
            public void changed(ObservableValue<? extends LearningApplication> observable, LearningApplication oldValue, LearningApplication newValue) {
                if(newValue != null) {
                    nameId.setText(newValue.getName());
                    descriptionId.setText(newValue.getDescription());
                    currentLAName = newValue.getName();
                }
            }
        };
        choiceBoxId.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }
}

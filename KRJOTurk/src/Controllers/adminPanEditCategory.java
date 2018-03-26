package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.*;

import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
//TODO: do it properly

public class adminPanEditCategory extends adminPanelController {

    private int currentLaID;
    private String currentLCname;

    @FXML
    ChoiceBox<LearningApplication> choiceBoxId;
    @FXML
    ChoiceBox<LearningCategory> choiceBoxCategoryId;
    @FXML
    Button deleteButton;
    @FXML
    Button confirmButton;
    @FXML
    TextField nameId;
    @FXML
    TextArea descriptionId;


    @FXML
    private void initialize() {
        editCategoryButton.setDisable(true);
        initChoiceBox();

        if (currentUser.getInstance().isCreator()){
            addAppButton.setVisible(false);
        }
    }

    public void saveButtonPressed() {
        if(getChoiceLC() == null) {
            AlertBox.display("This LC doesn't exist", "This Learning Category doesn't exist. " +
                    "Please create it in the panel \"Add Category\".");
            return;
        }

        if(nameId.getText().equals("")) {
            AlertBox.display("Add name", "Please add the name to this Category");
            return;
        }

        if(descriptionId.getText().equals("")) {
            AlertBox.display("Add description", "Please add the description to this Category");
            return;
        }

        if(systemData.getInstance().getLC(nameId.getText(), getChoiceLA().getId()) != null &&
                !nameId.getText().equals(currentLCname)) {
            AlertBox.display("LС already exists",
                    "Learning Category with this name is already exist. Please, choose another " +
                            "name or take the old one.");
        } else {
            systemData.getInstance().updateLC(nameId.getText(), descriptionId.getText(), getChoiceLC().getId());
            getChoiceLC().setName(nameId.getText());
            getChoiceLC().setDescription(descriptionId.getText());
            AlertBox.display("Success", "Changes was successfully added.");
            if(!nameId.getText().equals(currentLCname)) {
                TreeController.getInstance().updateTree();
                TreeController.getInstance().treeInitializer();
            }
        }
    }

    public void deleteButtonPressed() {
        systemData.getInstance().deleteLC(currentLCname, currentLaID);
        AlertBox.display("Deleted", "This Learning Category was successfully deleted.");
        initChoiceBox();
        TreeController.getInstance().updateTree();
        TreeController.getInstance().treeInitializer();
    }

    private void initChoiceBox() {
        //LA ChoiceBox init
        ArrayList<LearningApplication> LAs = systemData.getInstance().getDataLA();
        for(LearningApplication la : LAs) {
            if (currentUser.getInstance().isCreator() && (la.getCreatedBy() != currentUser.getInstance().getUser_id()))
                continue;
            choiceBoxId.getItems().add(la);
        }
        choiceBoxId.setValue(LAs.get(0));
        currentLaID = LAs.get(0).getId();
        initLCChoiceBox();

        //Change listener LA
        ChangeListener<LearningApplication> changeListenerLA = new ChangeListener<LearningApplication>() {
            @Override
            public void changed(ObservableValue<? extends LearningApplication> observable,
                                LearningApplication oldValue, LearningApplication newValue) {
                if(newValue != null) {
                    currentLaID = newValue.getId();
                    initLCChoiceBox();
                }
            }
        } ;
        choiceBoxId.getSelectionModel().selectedItemProperty().addListener(changeListenerLA);

        ChangeListener<LearningCategory> changeListenerLC = new ChangeListener<LearningCategory>() {
            @Override
            public void changed(ObservableValue<? extends LearningCategory> observable,
                                LearningCategory oldValue, LearningCategory newValue) {
                if(newValue != null) {
                    currentLCname = newValue.getName();
                    nameId.setText(newValue.getName());
                    descriptionId.setText(newValue.getDescription());
                }
            }
        };
        choiceBoxCategoryId.getSelectionModel().selectedItemProperty().addListener(changeListenerLC);
    }

    private void initLCChoiceBox() {
        choiceBoxCategoryId.getItems().clear();
        ArrayList<LearningCategory> LCs = getCurrentLCList();
        for(LearningCategory lc : LCs) {
            choiceBoxCategoryId.getItems().add(lc);
        }
        if (LCs.size() != 0) {
            choiceBoxCategoryId.setValue(LCs.get(0));
            currentLCname = LCs.get(0).getName();
            nameId.setText(LCs.get(0).getName());
            descriptionId.setText(LCs.get(0).getDescription());
        } else {
            AlertBox.display("This LA is empty", "There are no Learning Components in this " +
                    "Learning Application. Please, choose another one.");
            nameId.setText("");
            descriptionId.setText("");
            currentLCname = "";
        }
    }

    private ArrayList<LearningCategory> getCurrentLCList() {
        return (ArrayList<LearningCategory>) systemData.getInstance().getDataLC()
                .stream()
                .filter(lc -> lc.getLa_id() == currentLaID )
                .collect(toList());
    }

    private LearningApplication getChoiceLA() {
        return choiceBoxId.getValue();
    }

    private LearningCategory getChoiceLC() {
        return choiceBoxCategoryId.getValue();
    }
}

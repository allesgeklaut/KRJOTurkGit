package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.LearningInstance;

import service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateController extends Controller {

    private List<LearningUnit> myLUs;
    private int currentUserID = currentUser.getInstance().getUser_id();

    @FXML
    TreeView<LearningInstance> createTree;
    @FXML
    Button createLUButton;
    @FXML
    Button createLAButton;
    @FXML
    ComboBox<String> questionType;
    @FXML
    ComboBox<String> answerType;
    @FXML
    ListView<ColoredText> createdList;
    @FXML
    Label statementLabel1;
    @FXML
    Label statementLabel2;
    @FXML
    Label statementLabel3;

    @FXML
    private void initialize() throws SQLException {
        createButton.setDisable(true);
        buildTree(createTree);
        viewInit();
        if (currentUser.getInstance().isAdmin())
            createLAButton.setVisible(false);
        questionType.getItems().removeAll(questionType.getItems());
        questionType.getItems().addAll("Text", "Figure");
        answerType.getItems().removeAll(answerType.getItems());
        answerType.getItems().addAll("Text", "Figure");

        statementLabel1.setTextFill(Color.web("#33cc33"));
        statementLabel1.setText("green text: LU approved");

        statementLabel2.setTextFill(Color.web("#0033cc"));
        statementLabel2.setText("blue text: LU approval pending");

        statementLabel3.setTextFill(Color.web("#e60000"));
        statementLabel3.setText("red text: LU denied");

        // show LUs created by user
        getMyLUs();
        createdList.setCellFactory(lv -> new ListCell<ColoredText>() {
            @Override
            protected void updateItem(ColoredText item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setTextFill(null);
                } else {
                    setText(item.getText());
                    setTextFill(item.getColor());
                }
            }
        });

        for (LearningUnit LU : myLUs){
            LU.setNames();
            if (LU.getApprovedFlag() == 1)
                createdList.getItems().add(new ColoredText(LU.getName()+" ("+LU.getLAName()+"/"+LU.getCatName()+")",
                        Color.web("#33cc33")));
            else if (LU.getApprovedFlag() == 0)
                createdList.getItems().add(new ColoredText(LU.getName()+" ("+LU.getLAName()+"/"+LU.getCatName()+")",
                        Color.web("#0033cc")));
            else if (LU.getApprovedFlag() == -1)
                createdList.getItems().add(new ColoredText(LU.getName()+" ("+LU.getLAName()+"/"+LU.getCatName()+")",
                        Color.web("#e60000")));
        }
        //TODO JO edit created LUs?
    }

    public void createLAButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/userAddLA.fxml"));
            root = fxmlLoader.load();
            //create a new scene with root and set the stage
            double width = 600;
            double height = 400;

            // New stage init
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
            window.setTitle("Create LA");

            //Scene
            Scene scene = new Scene(root, width, height);
            //Start
            window.setScene(scene);
            window.show();
            // with this call the curser does not jump automatically into the first text field
            Platform.runLater(()->root.requestFocus());
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void createLUButtonClick(ActionEvent event) {
        //TODO JO make dependent on ComboBoxes -> and catch if nothing is set
        String qType = questionType.getValue();
        String aType = answerType.getValue();
        CreateLUBox CLUBox = new CreateLUBox();
        CLUBox.parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        CLUBox.display(qType, aType);
    }

    private void getMyLUs(){
        myLUs = new ArrayList<>();
        for (LearningUnit LU : systemData.getInstance().getLearningUnitList()){
            if (LU.getCreatedBy() == currentUserID){
                myLUs.add(LU);
            }
        }
    }
}

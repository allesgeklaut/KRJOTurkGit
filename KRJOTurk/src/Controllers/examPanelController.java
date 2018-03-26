package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class examPanelController extends Controller {
    @FXML
    Button createdExamsButton;
    @FXML
    Button createNewExamButton;

    public void createNewExamButtonClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/examViews/createNewExam.fxml");
        createNewExamButton.setDisable(true);
    }

    public void createdExamsButtonClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "/examViews/createdExams.fxml");
        createdExamsButton.setDisable(true);
    }
}

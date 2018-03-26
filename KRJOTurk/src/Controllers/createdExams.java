package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import service.Exam;
import service.systemData;


public class createdExams extends examPanelController{

    @FXML
    ListView<Exam> examsListView;

    private static final ObservableList list =
            FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        createdExamsButton.setDisable(true);
        initListView();
    }

    public void createNewExamButtonClicked() {

    }

    public void createdExamsButtonClicked() {

    }

    private void initListView() {
        list.clear();
        list.addAll(systemData.getInstance().getDataExams());
        examsListView.setItems(list);
    }
}

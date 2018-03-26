package Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import service.*;

import java.sql.SQLException;
import java.util.ArrayList;


public class LAController extends Controller{

    @FXML
    Button goToExamsButton;
    @FXML
    TextField searchTextField;
    @FXML
    Text LAName;
    @FXML
    ListView<LearningCategory> LCList;
    @FXML
    Text LADescription;
    @FXML
    TreeView<LearningInstance> LATree;
    @FXML
    private void initialize() throws SQLException {
        viewInit();
        buildTree(LATree);
        LearningInstance activeLI = systemData.getInstance().getActiveLI();
        LAName.setText(activeLI.getName());
        LADescription.setText(activeLI.getDescription());
        initializeLCListView();
    }

    public void searchOnAction() {
        ObservableList<LearningCategory> observableList = LCList.getItems();
        LearningInstance activeLI = systemData.getInstance().getActiveLI();
        ArrayList<LearningCategory> LCs = systemData.getInstance().getDataLC();
        observableList.remove(0, observableList.size());
        for(LearningCategory lc : LCs) {
            if (lc.getName().contains(searchTextField.getText()) && lc.getLa_id() == activeLI.getId())
                observableList.add(lc);
        }
    }

    private void initializeLCListView()
    {
        ObservableList<LearningCategory> observableList = LCList.getItems();
        LearningInstance activeLI = systemData.getInstance().getActiveLI();
        ArrayList<LearningCategory> LCs = systemData.getInstance().getDataLC();

        for (LearningCategory lc : LCs) {
            if (activeLI.getId() == lc.getLa_id())
                observableList.add(lc);
        }
        LCList.getSelectionModel().select(0);
        LCList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}

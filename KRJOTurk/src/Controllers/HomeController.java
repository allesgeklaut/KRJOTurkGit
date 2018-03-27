package Controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import service.LearningInstance;
import service.currentUser;
import service.systemData;

/**
 * Created by a1 on 28.10.17.
 */
public class HomeController extends Controller {
	@FXML
	ChoiceBox<String> searchChoiceBox;
	@FXML
	TextField searchTextField;
	@FXML
	ListView<LearningInstance> searchListView;
	@FXML
	TreeView<LearningInstance> homeTree;

	@FXML
	private void initialize() throws SQLException {
		viewInit();
		statementLabel.setText("Welcome Mr. " + currentUser.getInstance().getUser_surname());
		homeButton.setDisable(true);
		buildTree(homeTree);
		choiceBoxInitializer();
	}

	public void searchOnAction() {
		String searchText = searchTextField.getText();
		ArrayList<LearningInstance> output = new ArrayList<>();
		switch (getChoice()) {
		case "Learning Categories": {
			output = systemData.getInstance().search(2, searchText);
			break;
		}
		case "Learning Applications": {
			output = systemData.getInstance().search(1, searchText);
			break;
		}
		case "Learning Units": {
			output = systemData.getInstance().search(3, searchText);
		}
		}
		initializeCListView(output);
	}

	private void initializeCListView(ArrayList<LearningInstance> searchResult) {
		searchListView.getItems().clear();
		ObservableList<LearningInstance> observableList = searchListView.getItems();
		for (LearningInstance li : searchResult) {
			observableList.add(li);
		}
		searchListView.getSelectionModel().select(0);
		searchListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	// Return the searching area from ChoiceBox
	private String getChoice() {
		return searchChoiceBox.getValue();
	}

	// INITIALIZE FUNCTIONS :

	// Initialize choiceBox
	private void choiceBoxInitializer() {
		searchChoiceBox.getItems().addAll("Learning Applications", "Learning Categories", "Learning Units");
		searchChoiceBox.setValue("Learning Applications");
	}
}

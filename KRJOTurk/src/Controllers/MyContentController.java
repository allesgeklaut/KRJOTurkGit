package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import service.*;

import javafx.scene.text.Text;
import java.sql.*;
import java.util.ArrayList;

public class MyContentController extends Controller {

    private Stage window;
    String currentLAName;

    @FXML
    TreeView<LearningInstance> myContentTree;
    @FXML
    ListView<String> newLAListView;
    @FXML
    Button showButton;
    @FXML
    Button approveButton;
    @FXML
    Button denyButton;
    @FXML
    Text listViewLabel;

    @FXML
    private void initialize() throws SQLException {
        viewInit();
        myContentButton.setDisable(true);
        newLIs = new ArrayList<>();

        buildTree(myContentTree);
        if (currentUser.getInstance().isAdmin()){
            getNewLAs();
            getNewLUs();
            listViewLabel.setText("new Learning Applications / Units");
        }
        if (currentUser.getInstance().isCreator()){
            getNewLUs();
            listViewLabel.setText("new Learning Units");
        }

        // show LUs created by user
        newLAListView.getItems().clear();

        newLAListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        for (LearningInstance LI : newLIs){
            if (LI instanceof LearningApplication)
                newLAListView.getItems().add("LA: " + LI.getName());
            if (LI instanceof LearningUnit){
                ((LearningUnit) LI).setNames();
                newLAListView.getItems().add("LU (" + ((LearningUnit) LI).getLAName() + "/" +
                        ((LearningUnit) LI).getCatName() + "): " + LI.getName());
            }
        }
    }

    public void showButtonClick(ActionEvent event) throws SQLException {
        String selectedItem = newLAListView.getSelectionModel().getSelectedItem();
        currentLAName = parseString(selectedItem);
        LearningInstance activeLI;
        systemData.getInstance().setActiveLI(getChosenLI(currentLAName));
        activeLI = systemData.getInstance().getActiveLI();
        Parent root;

        Connection conn = systemData.getInstance().getDBConnection();
        ResultSet resultSet;
        LearningUnit LU = null;
        Statement statement = null;

        try {
            FXMLLoader fxmlLoader = null;
            if (activeLI instanceof LearningApplication){
                fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LA.fxml"));
            }
            else if (activeLI instanceof LearningUnit){
                switch (((LearningUnit) activeLI).getQuestionAnswerCombi()) {
                    case "tt":
                        fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUTextText.fxml"));
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery("SELECT * FROM lu_text_text WHERE id = " + activeLI.getId());
                        resultSet.next();
                        LU = new LuText(resultSet);

                        break;
                    case "ft":
                        fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUFigureText.fxml"));
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery("SELECT * FROM lu_figure_text WHERE id = " + activeLI.getId());
                        resultSet.next();
                        LU = new LuFigureText(resultSet, 'i');
                        break;
                    case "ff":
                        fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUFigureFigure.fxml"));
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery("SELECT * FROM lu_figure_figure WHERE id = " + activeLI.getId());
                        resultSet.next();
                        LU = new LuFigureFigure(resultSet, 'i');
                        break;
                }
            }

            assert fxmlLoader != null;
            root = fxmlLoader.load();
            //create a new scene with root and set the stage
            double width = 800;
            double height = 600;

            // hand over information to controller
            Controller controller = fxmlLoader.getController();
            controller.homeButton.setDisable(true);
            controller.adminPanelButton.setDisable(true);
            controller.examPanelButton.setDisable(true);
            controller.myContentButton.setDisable(true);
            controller.settingsButton.setDisable(true);

            controller.logOutButton.setDisable(true);

            // New stage init
            window = new Stage();
            if(activeLI instanceof LearningApplication){
                window.setTitle("New Learning Application View");
                ((LAController)controller).LATree.setVisible(false);
            }
            else {
                window.setTitle("New Learning Unit View");
                ((LUController)controller).LUTree.setVisible(false);
                LU.setLa_id(((LearningUnit)activeLI).getLa_id());
                ((LUController) controller).learningUnit = LU;
                ((LUController) controller).setUp();
            }

            //Scene
            Scene scene = new Scene(root, width, height);
            //Start
            window.setScene(scene);
            window.show();
            // with this call the curser does not jump automatically into the first text field
            Platform.runLater(root::requestFocus);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        initialize();
    }

    public void approvedButtonClick(ActionEvent event) throws SQLException {
        if (window == null){
            currentLAName = parseString(newLAListView.getSelectionModel().getSelectedItem());
            systemData.getInstance().setActiveLI(getChosenLI(currentLAName));
        }
        LearningInstance chosenItem = getChosenLI(currentLAName);

        Connection conn = systemData.getInstance().getDBConnection();
        // update LA
        String query = null;
        if (chosenItem instanceof LearningApplication)
            query = "update learning_applications set approved = ? where la_name = ?";
        else if (chosenItem instanceof LearningUnit)
            query = "update learning_units set approved = ? where refName = ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, "1");
        prepStmt.setString(2,currentLAName);
        prepStmt.executeUpdate();
        // make user to creator
        if (chosenItem instanceof LearningApplication){
            query = "update users set isCreator = ? where user_id = ?";
            prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, "1");
            prepStmt.setString(2, Integer.toString (((LearningApplication) chosenItem).getCreatedBy())) ;
            prepStmt.executeUpdate();
        }

        systemData.getInstance().reInit();
        if (window!=null){
            window.close();
            window = null;
        }
        initialize();
        systemData.getInstance().reInit();
        TreeController.getInstance().updateTree();
        TreeController.getInstance().treeInitializer();
    }

    public void denyButtonClick(ActionEvent event) throws SQLException {
        if (window == null){
            currentLAName = parseString(newLAListView.getSelectionModel().getSelectedItem());
            systemData.getInstance().setActiveLI(getChosenLI(currentLAName));
        }
        LearningInstance chosenItem = getChosenLI(currentLAName);

        Connection conn = systemData.getInstance().getDBConnection();
        // update LA
        String query = null;
        if (chosenItem instanceof LearningApplication)
            query = "update learning_applications set approved = ? where la_name = ?";
        else if (chosenItem instanceof LearningUnit)
            query = "update learning_units set approved = ? where refName = ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, "-1");
        prepStmt.setString(2,currentLAName);
        prepStmt.executeUpdate();

        systemData.getInstance().reInit();
        if (window!=null){
            window.close();
            window = null;
        }
        initialize();
    }

//    private LearningInstance getChosenLA(String name){
//        for (LearningApplication LA : newLas){
//            if (LA.getName().equals(name))
//                return LA;
//        }
//        return null;
//    }

    private LearningInstance getChosenLI(String name){
        for (LearningInstance LI : newLIs){
            if (LI.getName().equals(name))
                return LI;
        }
        return null;
    }

}



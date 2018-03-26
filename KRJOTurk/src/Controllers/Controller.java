package Controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**class which contains the standard controller functionality
 *
 */
public class Controller {
    @FXML
    Button homeButton;
    @FXML
    Button myContentButton;
    @FXML
    Button createButton;
    @FXML
    Button settingsButton;
    @FXML
    Button logOutButton;
    @FXML
    Button adminPanelButton;
    @FXML
    Label statementLabel;
    @FXML
    Button examPanelButton;

    Stage currentWindow = new Stage();
    List<LearningApplication> newLas;
    List<LearningUnit> newLUs;
    List<LearningInstance> newLIs = new ArrayList<>();

    protected static String toNumeralString(final Boolean input) {
        if (input == null) {
            return "0";
        } else {
            return input.booleanValue() ? "1" : "0";
        }
    }

    /**function to add a new scene to active stage
     *
     * @param window    corresponding window
     * @param newFxml   new .fxml file (scene) to load
     */

    protected void newScene(Stage window, String newFxml) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/view/" + newFxml));
            //create a new scene with root and set the stage
            double width = window.getScene().getWidth();
            double height = window.getScene().getHeight();
            Scene scene = new Scene(root, width, height);
            currentWindow = window;
            window.setScene(scene);
            window.show();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    void newScene(Stage window, Parent root) {
        //create a new scene with root and set the stage
        double width = window.getScene().getWidth();
        double height = window.getScene().getHeight();
        Scene scene = new Scene(root, width, height);
        window.setScene(scene);
        window.show();
    }



    /**function to set up tree on new scene
     *
     * @param tree  TreeView item of current screen
     */
    void buildTree(TreeView<LearningInstance> tree){
        TreeController.getInstance().mainTree = tree;
        TreeController.getInstance().treeInitializer();
    }

    // button functions

    public void adminPanelButtonClicked(ActionEvent event) {
        Parent root = null;
        try {
            Stage window = new Stage();
            window.setResizable(false);
            window.initModality(Modality.NONE); // block main stage during this stage is open
            window.setTitle("Admin panel");
            if (currentUser.getInstance().isAdmin())
                root = FXMLLoader.load(getClass().getResource("/resources/view/adminViews/adminPanAddApp.fxml"));
            else if(currentUser.getInstance().isCreator())
                root = FXMLLoader.load(getClass().getResource("/resources/view/adminViews/adminPanEditApp.fxml"));
            assert root != null;
            window.setScene(new Scene(root, 600, 400));
            window.show();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void examPanelButtonClicked(ActionEvent event) {
        Parent root;
        try {
            Stage window = new Stage();
            window.setResizable(false);
            window.initModality(Modality.NONE); // block main stage during this stage is open
            window.setTitle("Exam panel");
            root = FXMLLoader.load(getClass().getResource("/resources/view/examViews/createdExams.fxml"));
            window.setScene(new Scene(root, 600, 400));
            window.show();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void homeButtonClick(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "home.fxml");
    }

    public void settingsButtonClick(ActionEvent event) {
        Parent root;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/settings.fxml"));
            root = fxmlLoader.load();
            //create a new scene with root and set the stage
            double width = 600;
            double height = 400;

            // New stage init
            Stage window = new Stage();
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL); // block main stage during this stage is open
            window.setTitle("Settings");

            // hand over information to controller
            SettingsController controller = fxmlLoader.getController();
            controller.parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            //Scene
            Scene scene = new Scene(root, width, height);
            //Start
            window.setScene(scene);
            window.show();
            // with this call the curser does not jump automatically into the first text field
            Platform.runLater(root::requestFocus);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    void viewInit() throws SQLException {
        myContentButton.setVisible(false);
        if(!currentUser.getInstance().isAdmin()) {
            adminPanelButton.setVisible(false);
        }
        else{
            myContentButton.setVisible(true);
            myContentButton.setText("LU/LA Requests");
            //createButton.setVisible(false);
            getNewLAs();
            getNewLUs();
            if (newLIs.size() != 0)
                myContentButton.setTextFill(Color.web("#0033cc"));
        }

        if(!currentUser.getInstance().isAdmin() && !currentUser.getInstance().isCreator()) {
            examPanelButton.setVisible(false);
        }
        if(currentUser.getInstance().isCreator()){
            adminPanelButton.setVisible(true);
            adminPanelButton.setText("Creator Panel");
            myContentButton.setVisible(true);
            myContentButton.setText("LU Requests");
            getNewLUs();
            if (newLUs.size() != 0)
                myContentButton.setTextFill(Color.web("#0033cc"));
        }

    }


    public void logOutButtonClicked(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "login.fxml");
    }

    public void myContentButtonClick(ActionEvent event) {
        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "myContent.fxml");
    }

    public void createButtonClick(ActionEvent event) {

        newScene((Stage)((Node)event.getSource()).getScene().getWindow(), "create.fxml");

    }

    void getNewLAs() {
        newLas = new ArrayList<>();
        for (LearningApplication LA : systemData.getInstance().getDataLA()){
            if (LA.getApprovedFlag() == 0){
                newLas.add(LA);
                newLIs.add(LA);
            }
        }
    }

    void getNewLUs() throws SQLException {
        newLUs = new ArrayList<>();
        for (LearningUnit LU : systemData.getInstance().getLearningUnitList()){
            if (LU.getApprovedFlag() == 0){
                // get creator of corresponding LA
                Connection conn = systemData.getInstance().getDBConnection();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_applications WHERE la_id = " +
                        LU.getLa_id());
                resultSet.next();
                int creatorOfLA = resultSet.getInt("created_by");
                // skip if currentUser is not creator of LA
                if (currentUser.getInstance().isCreator() && creatorOfLA != currentUser.getInstance().getUser_id())
                    continue;
                newLUs.add(LU);
                newLIs.add(LU);
            }
        }
    }

    String  parseString(String in){

        String[] tokens = in.split(": ");
        return tokens[1];
    }
}

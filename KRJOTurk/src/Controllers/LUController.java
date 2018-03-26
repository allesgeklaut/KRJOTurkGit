package Controllers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LUController extends Controller {

    @FXML
    TreeView<LearningInstance> LUTree;
    @FXML
    public Text titleText;
    @FXML
    public Text questionText;
    @FXML
    public Text answerText1;
    @FXML
    public Text answerText2;
    @FXML
    public Text answerText3;
    @FXML
    public Text answerText4;
    @FXML
    public CheckBox checkBox1;
    @FXML
    public CheckBox checkBox2;
    @FXML
    public CheckBox checkBox3;
    @FXML
    public CheckBox checkBox4;
    @FXML
    public Button confirmButton;
    @FXML
    public Label textBox;
    @FXML
    Button editButton;

    public WrappedImageView questionPic;
    public WrappedImageView answerPic;
    @FXML
    VBox imageVBox;
    @FXML
    VBox qImageVBox;
    @FXML
    HBox aImageHBox1;
    @FXML
    HBox aImageHBox2;
    @FXML
    HBox aImageHBox3;

    LearningUnit learningUnit;
    private String correctAnswers;

    Map<String, LearningUnit> mapOFleaningUnit;
    List<LearningUnit> learningUnitList;

    Map<Integer, List<LearningUnit>> learningUnitMap;

    List<String> listForListView;

//    private Map<Integer, LearningUnit> learningUnitMap;
//    private List<LearningUnit> learningUnitList;



    @FXML
    private void initialize() throws SQLException {

        learningUnitMap = systemData.getInstance().getLearningUnitMap();
        createList(systemData.getInstance().getLastCategoryId());
        if (systemData.getInstance().getLearningUnitArrayList().size() > 0){
            createHashMap();
            ListProperty<String> listProperty = new SimpleListProperty<>();
            ObservableList<String> list = FXCollections.observableArrayList(createList());
            listProperty.set(FXCollections.observableArrayList(list));
            // systemData.getInstance().setExamFlag(false);

        }

        viewInit();
//        if(!currentUser.getInstance().isAdmin() && !currentUser.getInstance().isCreator()) {
//            adminPanelButton.setVisible(false);
//        }
        buildTree(LUTree);
        if (systemData.getInstance().getLastMessage() != null){
            textBox.setText(systemData.getInstance().getLastMessage());
        } else {
            textBox.setText("");
        }

    }

    private void createHashMap() {

        mapOFleaningUnit = new HashMap<String, LearningUnit>();

        for (int count = 0; count < learningUnitList.size(); count++) {

            mapOFleaningUnit.put(learningUnitList.get(count).getName(), learningUnitList.get(count));
        }


    }

    private List<String> createList() {

        List<String> returnList = new ArrayList<>();
        for (LearningUnit aLearningUnitList : learningUnitList) {
            if (aLearningUnitList.getApprovedFlag() == 1)
                returnList.add(aLearningUnitList.getName());
        }
        listForListView = new ArrayList<>(returnList);
        return returnList;
    }

    private void createList(int categoryId) {

        learningUnitList = learningUnitMap.get(categoryId);

    }




    public void confirmButtonClick(ActionEvent event) throws IOException {
        correctAnswers = learningUnit.correctAnswers;
        String inputAnswers;
        if (!(learningUnit instanceof LuFigureFigure)){
            inputAnswers = toNumeralString(checkBox1.isSelected()) + toNumeralString(checkBox2.isSelected()) +
                    toNumeralString(checkBox3.isSelected()) + toNumeralString(checkBox4.isSelected());
        }
        else{
            inputAnswers = toNumeralString(checkBox1.isSelected()) + toNumeralString(checkBox2.isSelected()) +
                    toNumeralString(checkBox3.isSelected());
        }
        if (inputAnswers.equals(correctAnswers)){
            textBox.setTextFill(Color.web("#33cc33"));
            textBox.setText("Answer correct");
            systemData.getInstance().setLastMessage("Answer correct");
            systemData.getInstance().setScore(1);
            systemData.getInstance().setMaxScore(1);

        }
        else{
            StringBuilder outputString = new StringBuilder("The correct answers are ");
            for (int i = 0; i < correctAnswers.length(); i++){
                if (correctAnswers.charAt(i) == '1'){
                    outputString.append(Integer.toString(i+1));
                    outputString.append(", ");
                }
            }
            if (outputString.toString().charAt(outputString.length()-2) == ','){
                outputString.setCharAt(outputString.length()-1, '\0');
                outputString.setCharAt(outputString.length()-2, '.');
            }
            else{
                outputString = new StringBuilder("No answer is correct.");
            }

            textBox.setTextFill(Color.web("#e60000"));
            textBox.setText("Wrong Answer; " + outputString.toString());
            systemData.getInstance().setLastMessage("Wrong Answer; " + outputString.toString());
            systemData.getInstance().setScore(0);
            systemData.getInstance().setMaxScore(1);
        }

        systemData.getInstance().getLearningUnitArrayList().remove(0);
        List<LearningUnit> learningUnitList = systemData.getInstance().getLearningUnitArrayList();


        if ((learningUnitList != null) && (learningUnitList.size() != 0)){

            startlU(learningUnitList.get(0).getName());

        } else {
            systemData.getInstance().setLastMessage(null);
            // open home screen after exam
            Parent root;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/home.fxml"));
            root = fxmlLoader.load();
            HomeController controller = fxmlLoader.getController();

            controller.statementLabel.setText(textBox.getText() + "       Total score = " + systemData.getInstance().getScore() + "/" + systemData.getInstance().getMaxScore());
            systemData.getInstance().setMaxScore(0);
            systemData.getInstance().setScore(0);

            newScene((Stage)((Node)event.getSource()).getScene().getWindow(), root);
        }
    }

    public void setUp() throws SQLException {
        // move redundant stuff in super class -> LearningUnit
        if(learningUnit instanceof LuText){
            titleText.setText(((LuText) learningUnit).titleText);
            questionText.setText(((LuText) learningUnit).questionText);
            answerText1.setText(((LuText) learningUnit).answerText1);
            answerText2.setText(((LuText) learningUnit).answerText2);
            answerText3.setText(((LuText) learningUnit).answerText3);
            answerText4.setText(((LuText) learningUnit).answerText4);
        }
        else if (learningUnit instanceof LuFigureText){
            if (((LuFigureText) learningUnit).questionFigure != null){
                questionPic = new WrappedImageView();
                imageVBox.getChildren().add(questionPic);
                questionPic.setImage(((LuFigureText) learningUnit).questionFigure);
            }
            else
                VBox.setVgrow(imageVBox, Priority.NEVER);

            titleText.setText(((LuFigureText) learningUnit).titleText);
            questionText.setText(((LuFigureText) learningUnit).questionText);
            answerText1.setText(((LuFigureText) learningUnit).answerText1);
            answerText2.setText(((LuFigureText) learningUnit).answerText2);
            answerText3.setText(((LuFigureText) learningUnit).answerText3);
            answerText4.setText(((LuFigureText) learningUnit).answerText4);
        }
        else if (learningUnit instanceof LuFigureFigure){
            if (((LuFigureFigure) learningUnit).questionFigure != null){
                questionPic = new WrappedImageView();
                qImageVBox.getChildren().add(questionPic);
                questionPic.setImage(((LuFigureFigure) learningUnit).questionFigure);
            }
            else
                VBox.setVgrow(qImageVBox, Priority.NEVER);

            titleText.setText(((LuFigureFigure) learningUnit).titleText);
            questionText.setText(((LuFigureFigure) learningUnit).questionText);

            int index = 1;
            if(((LuFigureFigure) learningUnit).answerFigure1 != null){
                answerPic = new WrappedImageView();
                aImageHBox1.getChildren().add(index, answerPic);
                answerPic.setImage(((LuFigureFigure) learningUnit).answerFigure1);
            }
            else{
                VBox.setVgrow(aImageHBox1, Priority.NEVER);
                checkBox1.setVisible(false);
            }
            if(((LuFigureFigure) learningUnit).answerFigure2 != null){
                answerPic = new WrappedImageView();
                aImageHBox2.getChildren().add(index, answerPic);
                answerPic.setImage(((LuFigureFigure) learningUnit).answerFigure2);
            }
            else{
                VBox.setVgrow(aImageHBox2, Priority.NEVER);
                checkBox2.setVisible(false);
            }
            if(((LuFigureFigure) learningUnit).answerFigure3 != null){
                answerPic = new WrappedImageView();
                aImageHBox3.getChildren().add(index, answerPic);
                answerPic.setImage(((LuFigureFigure) learningUnit).answerFigure3);
            }
            else{
                VBox.setVgrow(aImageHBox3, Priority.NEVER);
                checkBox3.setVisible(false);
            }
        }

        correctAnswers = learningUnit.correctAnswers;
        if (!(learningUnit instanceof LuFigureFigure)){
            if(answerText1.getText().equals(""))
                checkBox1.setVisible(false);
            if(answerText2.getText().equals(""))
                checkBox2.setVisible(false);
            if(answerText3.getText().equals(""))
                checkBox3.setVisible(false);
            if(answerText4.getText().equals(""))
                checkBox4.setVisible(false);
        }

        // display delete Button on correct LUs
        if (learningUnit.getCreatedBy() == currentUser.getInstance().getUser_id()){
            editButton.setVisible(true);
        }
        if (currentUser.getInstance().isAdmin())
            editButton.setVisible(true);
        Connection conn = systemData.getInstance().getDBConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_applications WHERE la_id = " +
                learningUnit.getLa_id());
        resultSet.next();
        int creatorOfLA = resultSet.getInt("created_by");
        if (creatorOfLA == currentUser.getInstance().getUser_id())
            editButton.setVisible(true);
    }

    public void editButtonClick(ActionEvent event){
        AreYouSureBox AYSB = new AreYouSureBox();
        AYSB.setParentStage((Stage)((Node)event.getSource()).getScene().getWindow());
        AYSB.display(learningUnit);
    }



    public void startlU(String newValue) {
        try {
            LearningUnit LU = null;
            FXMLLoader fxmlLoader = null;
            Connection conn = systemData.getInstance().getDBConnection();
            String answerQuestionCombi = Character.toString(mapOFleaningUnit.get(newValue).getQuestion_type()) +
                    Character.toString(mapOFleaningUnit.get(newValue).getAnswer_type());
            systemData.getInstance().setLastLUid(mapOFleaningUnit.get(newValue).getId());
            String LastLUID = Integer.toString(systemData.getInstance().getLastLUid());
            ResultSet resultSet;
            LUController luController;
            Parent root;

            if (answerQuestionCombi.equals("tt")) {
                //newScene((Stage) lisViewLU.getParent().getScene().getWindow(), "LU1.fxml");
                fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUTextText.fxml"));

                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM lu_text_text WHERE id = " + LastLUID);
                resultSet.next();
                LU = new LuText(resultSet);
            } else if (answerQuestionCombi.equals("ft")) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUFigureText.fxml"));

                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM lu_figure_text WHERE id = " + LastLUID);
                resultSet.next();
                LU = new LuFigureText(resultSet, 'i');
            } else if (answerQuestionCombi.equals("ff")) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/LUFigureFigure.fxml"));

                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM lu_figure_figure WHERE id = " + LastLUID);
                resultSet.next();
                LU = new LuFigureFigure(resultSet, 'i');
            }
//                else if (mapOFleaningUnit.get(newValue).getQuestion_type().equals(1)) {
//                    if (mapOFleaningUnit.get(newValue).getAnswer_type() == 0) {
//                        newScene((Stage) lisViewLU.getParent().getScene().getWindow(), "LU1.fxml");
//                    } else if (mapOFleaningUnit.get(newValue).getAnswer_type() == 1) {
//                        newScene((Stage) lisViewLU.getParent().getScene().getWindow(), "LU2.fxml");
//                    } else if (mapOFleaningUnit.get(newValue).getAnswer_type() == 2) {
//                        newScene((Stage) lisViewLU.getParent().getScene().getWindow(), "LU3.fxml");
//                    }
//                }
            // get la_id
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM learning_units WHERE id = " + LastLUID);
            resultSet.next();

//                        assert luController != null;
            assert fxmlLoader != null;
            root = fxmlLoader.load();
            luController = fxmlLoader.getController();
            LU.setLa_id(resultSet.getInt("la_id"));
            LU.setCreatedBy(resultSet.getInt("created_by"));
            luController.learningUnit = LU;
            luController.setUp();
            newScene((Stage) LUTree.getParent().getScene().getWindow(), root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }








//    private void createHashMap() {
//        learningUnitMap = new HashMap<Integer, LearningUnit>();
//
//        for (LearningUnit aLearningUnitList : learningUnitList) {
//
//            learningUnitMap.put(aLearningUnitList.getId(), aLearningUnitList);
//        }
//    }


}

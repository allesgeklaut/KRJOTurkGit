package Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.Text;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.*;

import javax.naming.InsufficientResourcesException;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class CategoriesController extends Controller {

//    private FXMLLoader fxmlLoader;
//    private Connection conn;
//    LearningUnit LU;

    @FXML
    Button goToExamsButton;
    @FXML
    ChoiceBox<String> ExamLC;
    @FXML
    TextField searchTextField;
    @FXML
    Text LCName;
    @FXML
    Text LCDescription;
    @FXML
    TreeView<LearningInstance> categoriesTree;

    @FXML
    ListView<String> lisViewLU;

    Map<Integer, List<LearningUnit>> learningUnitMap;
    List<LearningUnit> learningUnitList;
    List<String> listForListView;

    Map<String, LearningUnit> mapOFleaningUnit;

    // TODO JO only add approved LUs
    @FXML
    private void initialize() {
        try {
            buildTree(categoriesTree);
            viewInit();

            LCName.setText(systemData.getInstance().getActiveLI().getName());
            LCDescription.setText(systemData.getInstance().getActiveLI().getDescription());

            learningUnitMap = systemData.getInstance().getLearningUnitMap();
            // TODO JO check if lastCategoryId is ok
            createList(systemData.getInstance().getLastCategoryId());

            ListProperty<String> listProperty = new SimpleListProperty<>();
            ObservableList<String> list = FXCollections.observableArrayList(createList());
            listProperty.set(FXCollections.observableArrayList(list));
            lisViewLU.itemsProperty().bind(listProperty);
            lisViewLU.setVisible(true);

            createHashMap();

            lisViewLU.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    startlU(newValue);

                }
            });
        } catch (Exception e) {
        }
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
            newScene((Stage) lisViewLU.getParent().getScene().getWindow(), root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void createList(int categoryId) {

        learningUnitList = learningUnitMap.get(categoryId);
        fillExamChoiceBox(categoryId);

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

    private void createHashMap() {

        mapOFleaningUnit = new HashMap<String, LearningUnit>();

        for (int count = 0; count < learningUnitList.size(); count++) {

            mapOFleaningUnit.put(learningUnitList.get(count).getName(), learningUnitList.get(count));
        }


    }

    public void searchOnAction() {
        //TODO: implement search functionality
        System.out.println(searchTextField.getText());
    }

    public void setLCDescriptionClicked() {
        String str = ExamLC.getSelectionModel().getSelectedItem();

        Map<Integer, LearningUnit> learningUnitMap = systemData.getInstance().getMapIntLU();
        List<Exam> exams = systemData.getInstance().getDataExams();
        Exam exam = null;

        for (Exam exam1 : exams) {
            if (exam1.getName().equals(str)) {
                exam = exam1;
            }
        }

        ArrayList<LearningUnit> learningUnitList = new ArrayList<>();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Integer>>() {
        }.getType();

        List<Integer> luIDlist = gson.fromJson(exam.getLu(), listType);

        for (int i = 0; i < luIDlist.size(); i++) {
            learningUnitList.add(learningUnitMap.get(luIDlist.get(i)));
        }
        systemData.getInstance().setLearningUnitArrayList(learningUnitList);
        startlU(learningUnitList.get(0).getName());

    }


    private void fillExamChoiceBox(int category_id) {
        ExamLC.getItems().clear();

        ArrayList<Exam> dataExams = systemData.getInstance().getDataExams();

        Map<Integer, LearningUnit> learningUnitMap = systemData.getInstance().getMapIntLU();

        ArrayList<Exam> realExams = new ArrayList<>();
        List<Integer> luIDlist = new ArrayList<>();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Integer>>() {
        }.getType();


        for (int i = 0; i < dataExams.size(); i++) {

            luIDlist = gson.fromJson(dataExams.get(i).getLu(), listType);

            if ((luIDlist != null) || (luIDlist.size() != 0)) {
                LearningUnit learningUnit = learningUnitMap.get(luIDlist.get(0));
                if (category_id == learningUnit.getCategory_id()) {
                    ExamLC.getItems().add(dataExams.get(i).getName());
                    realExams.add(dataExams.get(i));
                }

            }


        }


    }

}



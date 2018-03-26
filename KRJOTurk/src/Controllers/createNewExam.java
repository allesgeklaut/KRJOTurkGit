package Controllers;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckListView;
import service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createNewExam extends examPanelController {
    @FXML
    ChoiceBox<String> choiceBoxLAId;

    private static final ObservableList list =
            FXCollections.observableArrayList();

    @FXML
    ChoiceBox<LearningCategory> choiceBoxLCId;
    @FXML
    Button confirmButton;
    @FXML
    TextField nameId;
    @FXML
    CheckListView<String> LUListViewId;
    Map<Integer, LearningUnit> learningUnitMap;
    CheckListView<String> LAListViewId;
    List<LearningApplication> la;
    HashMap<Integer, LearningUnit> learningUnitForExamList;
    List<LearningUnit> realList;

    boolean first = false;

    @FXML
    private void initialize() {
        la = systemData.getInstance().getDataLA();
        learningUnitMap = systemData.getInstance().getMapIntLU();


        for (int i = 0; i < la.size(); i++){
            choiceBoxLAId.getItems().add(la.get(i).getName());
        }

        choiceBoxLAId.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                int LC = la.get((Integer) number2).getId();
                setCategories(LC);
            }
        });

        generateLuListView(-1);


    }

    private void setCategories(int id){
        learningUnitForExamList = new HashMap<Integer, LearningUnit>();
        if (!first){
            first = true;
        } else {
            choiceBoxLCId.getItems().clear();
        }



        List<LearningCategory> arrayListCategory = systemData.getInstance().getDataLC();
        Map<String, LearningUnit> map = systemData.getInstance().getMapStringLU();

        if (id != -1){
            for (int i = 0; i < arrayListCategory.size(); i++){
                if (arrayListCategory.get(i).getLa_id() == id) {
                    choiceBoxLCId.getItems().add(arrayListCategory.get(i));
                }
            }
        } else {
            for (int i = 0; i < arrayListCategory.size(); i++){
                choiceBoxLCId.getItems().add(arrayListCategory.get(i));
            }
        }


        choiceBoxLCId.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                int LC = choiceBoxLCId.getItems().get((Integer) number2).getId();
                generateLuListView(LC);
            }
        });




    }


    private void generateLuListView(int id){
        LUListViewId.getItems().clear();

        List<LearningUnit> list = new ArrayList<LearningUnit>(learningUnitMap.values());
         realList = new ArrayList<LearningUnit>();


        final ObservableList<String> strings = FXCollections.observableArrayList();

        if (id != -1){
            for(int i = 0; i < list.size(); i++) {
                if ( list.get(i).getCategory_id()== id){
                    LUListViewId.getItems().add(list.get(i).getName());
                    realList.add(list.get(i));
                }

            }
        } else {
            for (int i = 0; i < list.size(); i++) {

                    LUListViewId.getItems().add(list.get(i).getName());
                realList.add(list.get(i));

            }
        }
    }
    public void confirmButtonPressed() {

       List<Integer> str =  LUListViewId.getCheckModel().getCheckedIndices();
       List<Integer> checkedList = new ArrayList<>();

       int e = 0;
       for (int i = 0; i < str.size(); i++){
           checkedList.add(realList.get(str.get(i)).getId());
       }

        String json = new Gson().toJson(checkedList);

        Exam exam = new Exam(nameId.getText(), systemData.getInstance().getDataExams().size() + 1, json);


        systemData.getInstance().saveExam(exam);
        systemData.getInstance().setExamData();

    }

}

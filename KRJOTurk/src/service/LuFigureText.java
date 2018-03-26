package service;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.ResultSet;

public class LuFigureText extends LearningUnit{

//    private int id;
//    private String text;
//    private ResultSet resultSet;

    public String titleText;
    public String questionText;
    public Image questionFigure;       //< save the image in class
    public String answerText1;
    public String answerText2;
    public String answerText3;
    public String answerText4;

    private void loadStuff(){
        try {
            setId(resultSet.getInt("id"));
            name = resultSet.getString("refName");
            titleText = resultSet.getString("title");
            questionText = resultSet.getString("question_text");
            answerText1 = resultSet.getString("answer1");
            answerText2 = resultSet.getString("answer2");
            answerText3 = resultSet.getString("answer3");
            answerText4 = resultSet.getString("answer4");
            correctAnswers = resultSet.getString("correctAnswers");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    LuFigureText(ResultSet resultSet){
        try {
            this.resultSet = resultSet;
            loadStuff();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public LuFigureText(ResultSet resultSet, char c){
        try {
            this.resultSet = resultSet;
            loadStuff();
            questionFigure = readImage("question_figure");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
}

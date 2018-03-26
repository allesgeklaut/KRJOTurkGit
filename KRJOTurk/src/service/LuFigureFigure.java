package service;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;

public class LuFigureFigure extends LearningUnit{

//    private int id;
//    private String text;
   // private ResultSet resultSet;

    public String titleText;
    public String questionText;
    public Image questionFigure;       //< save images in class
    public Image answerFigure1;
    public Image answerFigure2;
    public Image answerFigure3;

    private void loadStuff(){
        try {
            setId(resultSet.getInt("id"));
            name = resultSet.getString("refName");
            titleText = resultSet.getString("title");
            questionText = resultSet.getString("question_text");

            correctAnswers = resultSet.getString("correctAnswers");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public LuFigureFigure(ResultSet resultSet){
        try {
            this.resultSet = resultSet;
            loadStuff();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public LuFigureFigure(ResultSet resultSet, char c){
        try {
            this.resultSet = resultSet;
            loadStuff();
            questionFigure = readImage("question_figure");
            answerFigure1 = readImage("answer1");
            answerFigure2 = readImage("answer2");
            answerFigure3 = readImage("answer3");
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

package service;

import java.sql.ResultSet;

public class LuText extends LearningUnit{

//    private int id;
//    private String text;
    private ResultSet resultSet;

    public String titleText;
    public String questionText;
    public String answerText1;
    public String answerText2;
    public String answerText3;
    public String answerText4;

    public LuText(ResultSet resultSet){
        try {
            this.resultSet = resultSet;
            setId(resultSet.getInt("id"));
            name = resultSet.getString("refName");
            titleText = resultSet.getString("title");
            questionText = resultSet.getString("question");
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

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

}

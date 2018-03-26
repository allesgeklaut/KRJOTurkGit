package service;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

    // TODO JO put some members up in super class
public class LearningUnit extends LearningInstance{

    public LearningUnit(){}

    public LearningUnit(int id, String name, String description) {
        super(id, name, description);
    }

    private char question_type;
    private char answer_type;
    private int category_id;
    private String questionAnswerCombi;

    private int la_id;
    private int approvedFlag;
    private int createdBy;
    // not in database
    private String LAName;
    private String CatName;

    public String correctAnswers;
    ResultSet resultSet;

        public void setNames() throws SQLException {
        Connection conn = systemData.getInstance().getDBConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSetLA = statement.executeQuery("SELECT * FROM learning_applications WHERE la_id = " +
                Integer.toString(this.la_id));
        resultSetLA.next();
        LAName = resultSetLA.getString("la_name");
        ResultSet resultSetC = statement.executeQuery("SELECT * FROM learning_caterogies WHERE lc_id = " +
                Integer.toString(this.category_id));
        resultSetC.next();
        CatName = resultSetC.getString("lc_name");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    Image readImage(String databaseFigure){
        String path = "images";
        String questionFigurePath = path + File.separator + name + ".jpg";
        File theDir = new File(path);
        theDir.mkdir();
        Image returnImage = null;
        try {
            // to write image to filesystem
            FileOutputStream fos = new FileOutputStream(questionFigurePath);
            Blob blob = resultSet.getBlob(databaseFigure);
            File imageFile = new File(questionFigurePath);
            if (blob != null){
                int len = (int) blob.length();
                byte[] buf = blob.getBytes(1, len);
                fos.write(buf, 0, len);
                fos.close();
                returnImage = new Image(imageFile.toURI().toString());
                imageFile.delete();
            }
            else{
                fos.close();
                imageFile.delete();
            }

        }
        catch (Exception fe){
            fe.printStackTrace();
        }
        return returnImage;
    }

    public int getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(int approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public String getQuestionAnswerCombi() {
        return questionAnswerCombi;
    }

    //could send "" as description parameter

    public int getCategory_id() {

        return category_id;
    }
    public void setCategory_id(int category_id) {

        this.category_id = category_id;
    }
    public int getId() {

        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }
    public char getQuestion_type() {

        return question_type;
    }
    public void setQuestion_type(char question_type) {

        this.question_type = question_type;
        questionAnswerCombi = Character.toString(question_type) + Character.toString(answer_type);
    }
    public char getAnswer_type() {

        return answer_type;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setAnswer_type(char answer_type) {

        this.answer_type = answer_type;
        questionAnswerCombi = Character.toString(question_type) + Character.toString(answer_type);
    }

    public void setQuestionAnswerCombi(String questionAnswerCombi) {
        this.questionAnswerCombi = questionAnswerCombi;
    }

    public int getLa_id() {
        return la_id;
    }

    public void setLa_id(int la_id) {
        this.la_id = la_id;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getLAName() {
        return LAName;
    }


    public String getCatName() {
        return CatName;
    }

}


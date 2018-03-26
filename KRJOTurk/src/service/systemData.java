package service;

import java.sql.*;
import javax.swing.text.html.ListView;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class systemData { // Singeltion class
    private static systemData instance = new systemData();
    Map<String,String> loginData = new HashMap<>();
    ArrayList<User> users = new ArrayList<>();
    Statement statement;
    DBConnector connector = new DBConnector();

    String lastMessage;
    int score;
    int maxScore;

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = this.maxScore+ maxScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    ArrayList<LearningApplication> dataLA = new ArrayList<>();
    ArrayList<LearningCategory> dataLC = new ArrayList<>();
    ArrayList<Exam> dataExams = new ArrayList<>();

    ArrayList<LearningUnit> learningUnitArrayList = new ArrayList<>();

    public ArrayList<LearningUnit> getLearningUnitArrayList() {
        return learningUnitArrayList;
    }

    public void setLearningUnitArrayList(ArrayList<LearningUnit> learningUnitArrayList) {
        this.learningUnitArrayList = learningUnitArrayList;
    }

    private  Map<Integer, List<LearningUnit>> learningUnitMap;
    private Map<String, LearningUnit> mapStringLU;      //< map of all LUs (key=LU reference name; value=LU)
    private Map<Integer, LearningUnit> mapIntLU;            //< map of all LUs (key=LU id; value=LU)
    private List<LearningUnit> learningUnitList;
//    private Map<Integer, LuText> luTextMap;
//    private Map<Integer, LuDiagram> luDiagramMap;
//    private Map<Integer, LuTextPicture> luTextPictureMap;
//
//    public Map<Integer, LuText> getLuTextMap() {
//        return luTextMap;
//    }
//
//    public Map<Integer, LuDiagram> getLuDiagramMap() {
//        return luDiagramMap;
//    }
//
//    public Map<Integer, LuTextPicture> getLuTextPictureMap() {
//        return luTextPictureMap;
//    }


    public Map<Integer, LearningUnit> getMapIntLU() {
        return mapIntLU;
    }

    public List<LearningUnit> getLearningUnitList() {
        return learningUnitList;
    }

    private int lastCategoryId;
    private int lastLUid;

    public int getLastLUid() {
        return lastLUid;
    }

    public void setLastLUid(int lastLUid) {
        this.lastLUid = lastLUid;
    }

    public int getLastCategoryId() {
        return lastCategoryId;
    }

    public void setLastCategoryId(int lastCategoryId) {
        this.lastCategoryId = lastCategoryId;
    }

    public void setLearningUnitMap(Map<Integer, List<LearningUnit>> learningUnitMap) {
        this.learningUnitMap = learningUnitMap;
    }

        public Map<Integer, List<LearningUnit>> getLearningUnitMap() {
        return learningUnitMap;
    }

        public Map<String, LearningUnit> getMapStringLU() {
        return mapStringLU;
    }

    /** fills up information of the super class (LearningUnit)
     *
     */
    private LearningUnit fillSuperClassInfo(LearningUnit LU, ResultSet  resultSet) throws SQLException {
        LU.setQuestion_type(resultSet.getString("question_type").charAt(0));
        LU.setAnswer_type(resultSet.getString("answer_type").charAt(0));
        LU.setCategory_id(resultSet.getInt("category_id"));
        LU.setLa_id(resultSet.getInt("la_id"));
        LU.setApprovedFlag(resultSet.getInt("approved"));
        LU.setCreatedBy(resultSet.getInt("created_by"));

        return LU;
    }

    /** function to set a map of all learning units -> mapStringLU (key=LUName; value=LU)
     *
     */
    public void setMapStringLU() {
        mapStringLU = new HashMap<>();
        mapIntLU = new HashMap<>();
        try {
            String answerQuestionCombi;
            Connection conn = getDBConnection();
            Statement statement = conn.createStatement();
            Statement stSub = conn.createStatement();
            LearningUnit LUTemp;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_units");
            while (resultSet.next()){
                if (resultSet.getInt("approved") == 1){
                    answerQuestionCombi = resultSet.getString("question_type") +
                            resultSet.getString("answer_type");
                    switch (answerQuestionCombi){
                        case "tt":
                            ResultSet resultSetTT = stSub.executeQuery("SELECT * FROM lu_text_text WHERE id = " + Integer.toString(resultSet.getInt("id")));
                            //ResultSet resultSetTT = st.executeQuery("SELECT * FROM lu_text_text WHERE refName='test'");
                            resultSetTT.next();
                            LuText luText = new LuText(resultSetTT);
                            LUTemp = fillSuperClassInfo(luText, resultSet);
                            if (LUTemp instanceof LuText)
                                luText = (LuText) LUTemp;
                            mapStringLU.put(luText.getName(), luText);
                            mapIntLU.put(luText.getId(), luText);
                            break;
                        case "ft":
                            ResultSet resultSetFT = stSub.executeQuery("SELECT * FROM lu_figure_text WHERE id = " + Integer.toString(resultSet.getInt("id")));
                            //ResultSet resultSetTT = st.executeQuery("SELECT * FROM lu_text_text WHERE refName='test'");
                            resultSetFT.next();
                            LuFigureText luFigureText = new LuFigureText(resultSetFT, 'i');
                            LUTemp = fillSuperClassInfo(luFigureText, resultSet);
                            if (LUTemp instanceof LuFigureText)
                                luFigureText = (LuFigureText) LUTemp;
                            mapStringLU.put(luFigureText.getName(), luFigureText);
                            mapIntLU.put(luFigureText.getId(), luFigureText);
                            break;
                        case "ff":
                            ResultSet resultSetFF = stSub.executeQuery("SELECT * FROM lu_figure_figure WHERE id = " + Integer.toString(resultSet.getInt("id")));
                            //ResultSet resultSetTT = st.executeQuery("SELECT * FROM lu_text_text WHERE refName='test'");
                            resultSetFF.next();
                            LuFigureFigure luFigureFigure = new LuFigureFigure(resultSetFF, 'i');
                            LUTemp = fillSuperClassInfo(luFigureFigure, resultSet);
                            if (LUTemp instanceof LuFigureFigure)
                                luFigureFigure = (LuFigureFigure) LUTemp;
                            mapStringLU.put(luFigureFigure.getName(), luFigureFigure);
                            mapIntLU.put(luFigureFigure.getId(), luFigureFigure);
                            break;
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private int lastUserId;
    // TODO kind of redundant with class currentUser, but both are in use
    private int currentUserID;
    LearningInstance activeLI;



    private systemData() {
        connector.connectToDB();
        setUsersData();
        setLoginData();
        setDataLA();
        setDataLC();
        setLearningUnit();
        setExamData();

        // TODO maybe remove this call later
        setMapStringLU();
    }

    /** to reinitialize the systemData instance
     *
     */
    public void reInit(){
        instance = new systemData();
    }

    private void setLoginData() {
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM login_data");
            while(resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                loginData.put(username, password);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setExamData() {
        dataExams.clear();
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM exams");
            while(resultSet.next()) {
                String name = resultSet.getString("exam_name");
                String lu = resultSet.getString("learning_units");
                int id = resultSet.getInt("exam_id");
                dataExams.add(new Exam(name, id, lu));

            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveExam(Exam exam){

        String name = exam.getName();
        int id = exam.getId();
        String lu = exam.getLu();
        String query = "INSERT INTO exams VALUES (" + id + ", \"" + name + "\", \"" + lu + "\")";
        try {
            statement = connector.getConnection().createStatement();
            int resultSet = statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser() {
        for(User user : users) {
            if(user.user_id == currentUserID) {
                currentUser.setInstance(user);
            }
        }
    }



    public LearningApplication getLaByName(String name) {
        for(LearningApplication la : dataLA) {
            if(la.getName().equals(name))
               return la;
        }
        return null;
    }

    // keys :
    // 1 --> Learning App
    // 2 --> Learning Category
    // 3 --> Learning Unit
    public ArrayList<LearningInstance> search(int key, String word) {
        word = word.toLowerCase();
        ArrayList<LearningInstance> output = new ArrayList<>();
        switch (key) {
            case 1 : {
                for(LearningApplication la : dataLA) {
                    if(la.name.toLowerCase().contains(word) || la.description.toLowerCase().contains(word)) {
                        output.add(la);
                    }
                }
                break;
            }
            case 2 : {
                for(LearningCategory lc : dataLC) {
                    if(lc.name.toLowerCase().contains(word) || lc.description.toLowerCase().contains(word)) {
                        output.add(lc);
                    }
                }
                break;
            }
            case 3 : {
                for(LearningUnit lu : learningUnitList) {
                    if(lu.name.toLowerCase().contains(word))
                        output.add(lu);
                }
                break;
            }
        }
        return output;
    }

    private void setUsersData() {
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while(resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String user_name = resultSet.getString("user_name");
                String user_surname = resultSet.getString("user_surname");
                String email = resultSet.getString("email");
                Date birthdate = resultSet.getDate("birthdate");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                boolean isCreator = resultSet.getBoolean("isCreator");
                //String gender = resultSet.getString("gender");

                // User user = new User(id, user_name, user_surname, email, birthdate, gender);
                User user = new User(id, user_name, user_surname, email, birthdate, isAdmin, isCreator);
                users.add(user);
                lastUserId = id;

            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDataLA() {
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_applications");
            while(resultSet.next()) {
                int id = resultSet.getInt("la_id");
                String la_name = resultSet.getString("la_name");
                String la_description = resultSet.getString("la_description");
                int approvedFlag = resultSet.getInt("approved");
                int createdBy = resultSet.getInt("created_by");
                LearningApplication la = new LearningApplication(id, la_name, la_description, approvedFlag, createdBy);
                dataLA.add(la);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLA(String name, String description, int approvedFlag, int createdBy) {
        try {
            statement = connector.getConnection().createStatement();
            String query = "INSERT INTO learning_applications (la_name, la_description, approved, created_by) VALUES (\"" + name +
                    "\", \"" + description + "\", \"" + approvedFlag + "\", \"" + createdBy + "\")";
            statement.executeUpdate(query);
            ResultSet resultSet = statement.executeQuery("SELECT la_id FROM learning_applications");
            resultSet.last();
            int laId = resultSet.getInt("la_id");
            getDataLA().add(new LearningApplication(laId, name, description, approvedFlag, createdBy));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LearningCategory getLC(String name, int laId) {
        return dataLC.stream()
                .filter(lc -> lc.getLa_id() == laId && lc.getName().equals(name))
                .findFirst().orElse(null);
    }

    public void deleteLC(String name, int laId) {
        deleteLCFromDB(getLC(name, laId).getId());

        dataLC = (ArrayList<LearningCategory>) dataLC.stream()
                .filter(lc -> lc != getLC(name, laId))
                .collect(toList());
    }

    public void addLC(String name, String description, String laName) {
        try {
            int laID = getLaByName(laName).getId();
            statement = connector.getConnection().createStatement();
            String query = "INSERT INTO learning_caterogies (lc_name, lc_description, la_id) VALUES (\"" + name + "\", \""
                    + description + "\",\"" + laID + "\")";
            statement.executeUpdate(query);
            ResultSet resultSet = statement.executeQuery("SELECT lc_id FROM learning_caterogies");
            resultSet.last();
            int lcId = resultSet.getInt("lc_id");
            getDataLC().add(new LearningCategory(lcId, name, description, laID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateLA(String name, String description, int laId) {
        try {
            statement = connector.getConnection().createStatement();
            String query = "UPDATE learning_applications SET la_name = \"" + name + "\", la_description = \"" +
                    description + "\" WHERE la_id = "+ laId + ";";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLC(String name, String description, int lcId) {
        try {
            statement = connector.getConnection().createStatement();
            String query = "UPDATE learning_caterogies SET lc_name = \"" + name + "\", lc_description = \"" +
                    description + "\" WHERE lc_id = "+ lcId + ";";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeLAByName(String name) {
        deleteLAFromDB(getLaByName(name).getId());

        dataLA = (ArrayList<LearningApplication>) dataLA.stream()
                .filter(la -> !la.getName().equals(name))
                .collect(toList());

    }

    public ArrayList<Exam> getDataExams() {
        return dataExams;
    }

    public void setDataExams(ArrayList<Exam> dataExams) {
        this.dataExams = dataExams;
    }

    private void deleteLAFromDB(int id) {
        try {
            statement = connector.getConnection().createStatement();
            String query = "DELETE FROM learning_applications WHERE la_id = \"" + id + "\";";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deleteLCFromDB(int id) {
        try {
            statement = connector.getConnection().createStatement();
            String query = "DELETE FROM learning_caterogies WHERE lc_id = \"" + id + "\";";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user, String username, String password) {
        try {
            int creator = user.isCreator ? 1 : 0;
            statement = connector.getConnection().createStatement();
            String query = "INSERT INTO users (user_name, user_surname, email, birthdate, isCreator) VALUES (\"" +
                    user.getUser_name() + "\", \"" + user.getUser_surname() + "\", \"" + user.getEmail() + "\", \"" +
                    user.getBirthdate() + "\", \"" + creator + "\")";
            statement.executeUpdate(query);
            setLastUserId(getLastUserId()+1);
            // KRJO: changed this a bit, login_data user_id should not depend on users user_id
            // get UserID
            ResultSet resultSet = statement.executeQuery("SELECT user_id FROM users");
            resultSet.last();
            int UserId = resultSet.getInt("user_id");

            String query2 = "INSERT INTO login_data (username, password, user_id) VALUES (\"" + username +
                    "\", \"" + password + "\", \"" + UserId + "\")";
            statement.executeUpdate(query2);
            statement.close();
            users.add(user);
            loginData.put(username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setDataLC() {
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_caterogies");
            while(resultSet.next()) {
                int lc_id = resultSet.getInt("lc_id");
                int la_id = resultSet.getInt("la_id");
                String lc_name = resultSet.getString("lc_name");
                String lc_description = resultSet.getString("lc_description");
                LearningCategory lc = new LearningCategory(lc_id, lc_name, lc_description, la_id);
                dataLC.add(lc);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static systemData getInstance() {
        return instance;
    }

    public  ArrayList<User> getUsers() {
        return users;
    }

    public boolean isLoginSuccessful(String username, String password) {
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM login_data");
            for(Map.Entry<String, String> entry : loginData.entrySet()){
                if (entry.getKey().equals(username) && entry.getValue().equals(password)){
                    // KRJO: to set the user id of the current user to know who is logged in
                    while (resultSet.next())
                        if (resultSet.getString("username").equals(username)){
                            currentUserID = resultSet.getInt("user_id");
                            break;
                        }

                    statement.close();
                    setCurrentUser();
                    return true;
                }
            }
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<LearningApplication> getDataLA() {
        return dataLA;
    }

    public boolean isUsernameExist(String username) {
        for(Map.Entry<String, String> entry : loginData.entrySet())
            if (entry.getKey().equals(username))
                return true;
        return false;
    }

    public boolean isEmailExist(String email) {
        for(User user : users)
            if(user.getEmail().equals(email))
                return true;
        return false;
    }

    public ArrayList<LearningCategory> getDataLC() {
        return dataLC;
    }

    public int getLastUserId() {
        return lastUserId;
    }

    public int getCurrentUserID() {
        return currentUserID;
    }
    public void setCurrentUserID(int userId) {
        currentUserID = userId;
    }

    /** returns some data from the data base
     *
     * @param UserID user id of the user from which the data should be received
     * @param tableName table from which the data is of interest
     * @param columnNme column of that table
     */
    public String getDBData(int UserID, String tableName, String columnNme){
        String output = "";
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while(resultSet.next()) {
                if (resultSet.getInt("user_id") == UserID){
                    output = resultSet.getString(columnNme);
                    break;
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }
    public int getCategoryID(String refName, String tableName, String returnColumn, String searchColumn){
        int output = 0;
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while(resultSet.next()) {
                if (resultSet.getString(searchColumn).equals(refName)){
                    output = resultSet.getInt(returnColumn);
                    break;
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }
    public String getStringData(String name, String tableName, String returnColumn, String searchColumn){
        String output = "";
        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while(resultSet.next()) {
                if (resultSet.getString(searchColumn).equals(name)){
                    output = resultSet.getString(returnColumn);
                    break;
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void setLastUserId(int lastUserId) {
        this.lastUserId = lastUserId;
    }

    public Connection getDBConnection(){
            return connector.getConnection();
    }


    public LearningInstance getActiveLI() {
        return activeLI;
    }

    public void setActiveLI(LearningInstance activeLI) {
        this.activeLI = activeLI;
    }


    private void setLearningUnit(){
        learningUnitList = new ArrayList<>();

        try {
            statement = connector.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM learning_units");
            while(resultSet.next()) {
                LearningUnit learningUnit = new LearningUnit(resultSet.getInt("id"),
                        resultSet.getString("refName"), "");
              //  learningUnit.setId(;
                // TODO JO for now the reference name is shown frontend, maybe that's ok though!
              //  learningUnit.setName();
                learningUnit.setQuestion_type(resultSet.getString("question_type").charAt(0));
                learningUnit.setAnswer_type(resultSet.getString("answer_type").charAt(0));
                learningUnit.setCategory_id( resultSet.getInt("category_id"));
                learningUnit.setApprovedFlag(resultSet.getInt("approved"));
                learningUnit.setLa_id(resultSet.getInt("la_id"));
                learningUnit.setCreatedBy(resultSet.getInt("created_by"));
                learningUnitList.add(learningUnit);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        learningUnitMap = new HashMap<Integer, List<LearningUnit>>();

        for (int countCategorys = 0; countCategorys < dataLC.size(); countCategorys++){
            List<LearningUnit> learningUnitL = new ArrayList<>();
            for (int count = 0; count < learningUnitList.size(); count++){
                if (learningUnitList.get(count).getCategory_id() == dataLC.get(countCategorys).getId()){
                    learningUnitL.add(learningUnitList.get(count));
                }
            }
            learningUnitMap.put(dataLC.get(countCategorys).getId(), learningUnitL);
        }

        // setOther();
    }
// TODO JO dont know if still needed
//    private void setOther(){
//
//        luDiagramMap = new HashMap<>();
//        luTextMap = new HashMap<>();
//        luTextPictureMap = new HashMap<>();
//
//        try {
//            statement = connector.getConnection().createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM lu_diagram");
//            while(resultSet.next()) {
//                LuDiagram luDiagram = new LuDiagram();
//                luDiagram.setId(resultSet.getInt("id"));
//                luDiagram.setObject1_name(resultSet.getString("object1_name"));
//                luDiagram.setObject1_parameters(resultSet.getString("object1_parameters"));
//                luDiagram.setObject1_num( resultSet.getInt("object1_num"));
//                luDiagram.setObject2_name(resultSet.getString("object2_name"));
//                luDiagram.setObject2_num(resultSet.getInt("object2_num"));
//                luDiagram.setObject2_parameters(resultSet.getString("object2_parameters"));
//                luDiagram.setRelationship( resultSet.getString("relationship"));
//
//                luDiagramMap.put(luDiagram.getId(), luDiagram);
//            }
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
////        try {
////            statement = connector.getConnection().createStatement();
////            ResultSet resultSet = statement.executeQuery("SELECT * FROM lu_text_text");
////            while(resultSet.next()) {
////                LuText luText = new LuText();
////                luText.setId(resultSet.getInt("id"));
////                luText.setText(resultSet.getString("text"));
////
////
////                luTextMap.put(luText.getId(), luText);
////            }
////            statement.close();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//
//        try {
//            statement = connector.getConnection().createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM lu_text_picture");
//            while(resultSet.next()) {
//                LuTextPicture luTextPicture = new LuTextPicture();
//                luTextPicture.setId(resultSet.getInt("id"));
//                luTextPicture.setType(resultSet.getInt("type"));
//                luTextPicture.setText1(resultSet.getString("text1"));
//                luTextPicture.setText2( resultSet.getString("text2"));
//                luTextPicture.setText3(resultSet.getString("text3"));
//                luTextPicture.setText4(resultSet.getString("text4"));
//
//                luTextPictureMap.put(luTextPicture.getId(), luTextPicture);
//            }
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//    }
}



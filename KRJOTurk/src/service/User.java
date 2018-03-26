package service;

import java.sql.Date;

public class User {
    int user_id;
    String user_name;
    String user_surname;
    String email;
    Date birthdate;
    String gender;
    boolean isAdmin;
    boolean isCreator;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public User() {

    }
    //    public User(int user_id, String user_name, String user_surname, String email, Date birthdate, String gender) {
//        this.user_id = user_id;
//        this.user_name = user_name;
//        this.user_surname = user_surname;
//        this.email = email;
//        this.birthdate = birthdate;
//        this.gender = gender;
//    }

    public User(int user_id, String user_name, String user_surname, String email, Date birthdate) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.email = email;
        this.birthdate = birthdate;
    }

    public User(int user_id, String user_name, String user_surname, String email, Date birthdate, boolean isAdmin, boolean isCreator) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isAdmin = isAdmin;
        this.isCreator = isCreator;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) {this.gender = gender; }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_surname='" + user_surname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", gender=" + gender +
                '}';
    }
}

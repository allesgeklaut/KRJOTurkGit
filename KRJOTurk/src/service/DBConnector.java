package service;


import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

//Connect local MySQL Database to the program
public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/OADTurk?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DBConnector() {}

    public void connectToDB(){
        connect(URL, USERNAME, PASSWORD);

    }

    public void connect(String url, String username, String password){
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(url, username, password );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

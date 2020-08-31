package parcer.dbModule;


import org.sqlite.JDBC;

import java.sql.*;

public class DBHandler {

    private static final String CON_STR = "jdbc:sqlite:test.db";

    private static DBHandler instance = null;
    private final Connection connection;

    public static DBHandler getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) instance = new DBHandler();
        return instance;
    }

    private DBHandler() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(CON_STR);
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'study' ('groupName' TEXT , 'studyName' TEXT, 'studyDates' TEXT);");
        statement.close();
        System.out.println("База данных инициализирована");
    }

    public void insertStudy(String groupName, String studyName, String studyDates) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO 'study' ('groupName', 'studyName', 'studyDates') VALUES ('" + groupName + "', '" + studyName + "', '" + studyDates + "');");
        statement.close();
    }

    
}

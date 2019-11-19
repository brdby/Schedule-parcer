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
        statement.execute("CREATE TABLE if not exists 'group' ('id' INTEGER PRIMARY KEY, 'name' TEXT);");
        statement.execute("CREATE TABLE if not exists 'study' ('id' INTEGER , 'name' TEXT, FOREIGN KEY ('id') REFERENCES 'group' ('id'));");
        statement.close();
        System.out.println("База данных инициализирована");
    }

    public void insertGroup(String groupName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO 'group' ('name') VALUES ('" + groupName + "');");
        statement.close();
    }

    public void insertStudy(String groupName, String studyName) throws SQLException {
        int id;

        Statement statement = connection.createStatement();
        ResultSet group = statement.executeQuery("SELECT 'id' FROM 'group' WHERE 'name' = '" + groupName + "';");
        if (group.next()){
            id = group.getInt("id");
        }
        else return;
        statement.execute("INSERT INTO 'study' ('id', 'name') VALUES ('" + id + "', '" + studyName + "');");
        statement.close();
    }

    
}

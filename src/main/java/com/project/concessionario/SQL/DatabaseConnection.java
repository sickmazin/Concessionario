package com.project.concessionario.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    protected static Statement statement;

    public DatabaseConnection() throws SQLException {
        connectToDataBase();
    }

    public void connectToDataBase() throws SQLException {
        Connection connection= null;
        connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/concessionario",
                    "root",
                    "mattia02"
        );
        statement = connection.createStatement();
    }
}

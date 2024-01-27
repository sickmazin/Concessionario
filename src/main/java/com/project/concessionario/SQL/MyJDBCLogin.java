package com.project.concessionario.SQL;


import com.project.concessionario.ErrorAlert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyJDBCLogin extends  DatabaseConnection {

    public MyJDBCLogin() throws SQLException {
        super();
    }

    public boolean verificaUtente(String id, String password) {
        ResultSet resultSet;
        try {
            statement.executeQuery("SELECT * FROM `concessionario`.`login` WHERE password = '"+password+"' AND ID = '"+id+"';");
            resultSet = statement.getResultSet();
            return resultSet.next();

        } catch (SQLException e) {
            ErrorAlert errorAlert = new ErrorAlert(ErrorAlert.TYPE.SQL_EXCEPTION);
            errorAlert.show();
            e.printStackTrace();
            return false;
        }
    }
}

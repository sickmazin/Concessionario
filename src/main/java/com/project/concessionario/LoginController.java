package com.project.concessionario;


import com.project.concessionario.SQL.MyJDBCLogin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField password;
    private VBox tipoCorrente = null;

    @FXML
    void cambiaStile(MouseEvent event) {
        VBox vbox = (VBox) event.getSource();
        if (vbox != tipoCorrente) {
            vbox.setStyle("-fx-background-color: grey");
            if (tipoCorrente!=null) tipoCorrente.setStyle("-fx-background-color: #333736");
            tipoCorrente = vbox;
        }
        else {
            vbox.setStyle("-fx-background-color: #333736");
            tipoCorrente = null;
        }
    }

    @FXML
    void login(MouseEvent event) {
        ErrorAlert errorAlert;

        if (tipoCorrente == null || password.getText().equals("")) {
            errorAlert = new ErrorAlert(ErrorAlert.TYPE.WRONG_LOGIN);
            errorAlert.show();
            return;
        }
        loginDB();
    }
    private void loginDB(){
        ErrorAlert errorAlert;

        MyJDBCLogin login = null;

        try {
            login = new MyJDBCLogin();
        } catch (SQLException e) {
            errorAlert = new ErrorAlert(ErrorAlert.TYPE.SQL_EXCEPTION);
            errorAlert.show();
            return;
        }

        Text testo = (Text) tipoCorrente.getChildren().get(1);

        boolean utenteVerificato = login.verificaUtente(testo.getText(), password.getText());
        if (utenteVerificato) {
            Stage stage = (Stage) password.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app.fxml"));

            try {
                Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
                stage.setTitle("Concessionario");
                stage.setScene(scene);
                stage.show();
                ((VeicoloController) fxmlLoader.getController()).setTipo(testo.getText());

            } catch (IOException e) {
                errorAlert = new ErrorAlert(ErrorAlert.TYPE.FXML_ERROR);
                errorAlert.show();
            }
        }

        else {
            errorAlert = new ErrorAlert(ErrorAlert.TYPE.WRONG_LOGIN);
            errorAlert.show();
        }
    }
    @FXML
    void pressedEnterForLogin(KeyEvent event) {
        ErrorAlert errorAlert;


        if (event.getCode().equals(KeyCode.ENTER)){
            if (tipoCorrente == null || password.getText().equals("")) {
                errorAlert = new ErrorAlert(ErrorAlert.TYPE.WRONG_LOGIN);
                errorAlert.show();
                return;
            }
            loginDB();
        }
    }

}

package com.project.concessionario;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class ErrorAlert extends Alert {
    public enum TYPE {
        SQL_EXCEPTION("Errore nella connessione al database"),
        WRONG_LOGIN("Errore nel login:\npassword o ruolo errati"),
        FOREIGN_KEY("Errore nella selezione Modello-Marca: associazione di modello e marca errata."),
        FXML_ERROR("Errore nel caricamento del'applicazione, riprovare"),
        ILLEGAL_ARGS("Errore nell'inserimento dei parametri del veicolo"),
        PRIMARY_KEY("Errore:\n stai inserendo un veicolo con un numero di telaio già presente nel database");
        private String text;
        TYPE(String string) {
            text = string;
        }
    }
    public ErrorAlert(TYPE type) {
        super(AlertType.ERROR, type.text, ButtonType.CLOSE);
        setTitle("Errore");
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        dialogPane.setMinSize(500, 200);
    }

    public ErrorAlert(String string) {
        super(AlertType.ERROR, string, ButtonType.CLOSE);
        setTitle("Errore");
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        dialogPane.setMinSize(500, 200);
    }

}

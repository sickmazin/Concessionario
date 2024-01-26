package com.project.concessionario;

import com.project.concessionario.sidebarState.HideState;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;

public class LoginController {

    private enum TIPO {AUTO, VENDITE, CONTABILE, ADMIN, ASSISTENZA, NOLEGGIO, MAGAZZINIERE}
    private HashMap<VBox, TIPO> listaElementi = new HashMap<>();
    private TIPO tipoCorrente = null;


    @FXML
    void cambiaStile(MouseEvent event) {
        VBox vbox = (VBox) event.getSource();
        //vbox.setStyle("fx-background-color: ");
    }


    public void initialize() {

    }
}

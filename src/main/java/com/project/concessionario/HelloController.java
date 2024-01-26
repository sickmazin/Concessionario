package com.project.concessionario;

import com.project.concessionario.sidebarState.HideState;
import com.project.concessionario.sidebarState.TransitionState;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;



public class HelloController {
    @FXML
    private ChoiceBox<String> carburanteCB;
    @FXML
    private TextField descrizioneTF;
    @FXML
    private Button inserisci;
    @FXML
    private ChoiceBox<String> marcaCB;
    @FXML
    private AnchorPane menu;
    @FXML
    private TextField modelloTF;
    @FXML
    private ChoiceBox<String> ordinaCB;
    @FXML
    private ChoiceBox<String> posizioneCB;
    @FXML
    private ChoiceBox<String> possibilitaCB;
    @FXML
    private TextField prezzoTF;
    @FXML
    private ChoiceBox<String> sceltaFiltroCB;
    @FXML
    private TextField telaioTF;
    @FXML
    private Button visualizza;
    @FXML
    private ImageView ham;
    private TransitionState ts;




    @FXML
    void cambiaIconaMenu(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED)
            ham.setImage(new Image(System.getProperty("user.dir")+"\\src\\main\\resources\\icon\\Hamburger_grey.png"));
        else ham.setImage(new Image(System.getProperty("user.dir")+"\\src\\main\\resources\\icon\\Hamburger_white.png"));
    }
    @FXML
    void showMenu(MouseEvent event) {
        ts=(TransitionState) ts.nextState();
        ts.show();
    }

    public void initialize() {
        ts=new HideState();
        ts.setNode(menu);
        ts.setTransition(new TranslateTransition(Duration.seconds(0.35),menu));
        ts.setFrom(0);
        ts.setTo(-134);

        ts.show();
    }
}
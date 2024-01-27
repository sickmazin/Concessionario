package com.project.concessionario;

import com.project.concessionario.Prodotti.UnitaVeicolo;
import com.project.concessionario.SQL.MyJDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Modifica {
    @FXML
    private TextField agg1;
    @FXML
    private TextField agg2;
    @FXML
    private TextField agg3;
    @FXML
    private Text carburanteT;
    @FXML
    private TextArea descrizioneTA;
    @FXML
    private Text marcaT;
    @FXML
    private Text modelloT;
    @FXML
    private Button modifica;
    @FXML
    private ChoiceBox<?> posizioneCB;
    @FXML
    private TextField prezzoTF;
    @FXML
    private Text telaioT;
    @FXML
    private Text tipologiaT;
    private UnitaVeicolo veicolo;
    private MyJDBC database;
    private HashMap<String, String> modifiche = new HashMap<>();

    public void setDatabase(MyJDBC database) {
        this.database = database;
    }
    public void setVeicolo(UnitaVeicolo veicolo) {
        this.veicolo = veicolo;
        telaioT.setText(veicolo.getNumeroTelaio());
        marcaT.setText(veicolo.getMarca());
        modelloT.setText(veicolo.getModello());
        /*tipologiaT.setText(veicolo.getTipologia());
        * switch(veicolo.getTipologia()) {
        *   case ->
        *   case ->
        *   case ->
        *   case ->
        * }
        * */
        marcaT.setText(veicolo.getMarca());
        carburanteT.setText(veicolo.getCarburante());
        descrizioneTA.setText(veicolo.getDescrizione());
    }

    @FXML
    void modifica(MouseEvent event) {

        //MyJDBC.modifica(String telaio,
    }
}

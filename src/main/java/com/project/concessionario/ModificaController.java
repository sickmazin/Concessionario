package com.project.concessionario;

import com.project.concessionario.Prodotti.UnitaVeicolo;
import com.project.concessionario.SQL.MyJDBC;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;

public class ModificaController {
    @FXML
    private VBox vBoxAgg1;
    @FXML
    private VBox vBoxAgg2;
    @FXML
    private VBox vBoxAgg3;
    @FXML
    private Text carburanteT;
    @FXML
    private TextArea descrizioneTA;
    @FXML
    private Text marcaT;
    @FXML
    private Text modelloT;
    @FXML
    private ChoiceBox<String> posizioneCB;
    @FXML
    private TextField prezzoTF;
    @FXML
    private Text telaioT;
    @FXML
    private Text tipologiaT;
    @FXML
    private DatePicker data;
    @FXML
    private ChoiceBox<String> statoRipCB;
    private MyJDBC database;
    private HashMap<String, String> modifiche = new HashMap<>();
    private String labelSuTipologia;

    public void setDatabase(MyJDBC database) {
        this.database = database;
        for (String posizione : database.getPOSIZIONI()) posizioneCB.getItems().add(posizione);
        for (String s : database.getSTATO_RIPARAZIONE()) statoRipCB.getItems().add(s);
    }
    public void setVeicolo(UnitaVeicolo veicolo) {
        telaioT.setText(veicolo.getNumeroTelaio());
        marcaT.setText(veicolo.getMarca());
        modelloT.setText(veicolo.getModello());
        tipologiaT.setText(veicolo.getTipologia());
        marcaT.setText(veicolo.getMarca());
        carburanteT.setText(veicolo.getCarburante());
        descrizioneTA.setText(veicolo.getDescrizione());
        prezzoTF.setText(veicolo.getPrezzo()+"");

        switch(veicolo.getTipologia()) {
            case "Usato" -> {
                vBoxAgg1.setVisible(true);
                ((Label) vBoxAgg1.getChildren().get(0)).setText("Chilometraggio");
                ((TextField) vBoxAgg1.getChildren().get(1)).setText(veicolo.getChilometraggio()+"");
                labelSuTipologia = "Chilometraggio";
            }
            case "Veicolo da riparare" -> {
                vBoxAgg1.setVisible(true);
                ((Label) vBoxAgg1.getChildren().get(0)).setText("Descrizione danni");
                ((TextField) vBoxAgg1.getChildren().get(1)).setText(veicolo.getDescrizioneDanno());
                vBoxAgg2.setVisible(true);
                vBoxAgg3.setVisible(true);
                statoRipCB.setValue(veicolo.getStatoRiparazione());
                labelSuTipologia = "DescrizioneDanni";
            }
            default -> { }
        }
    }

    @FXML
    void modifica(MouseEvent event) {
        modifiche.put("NumeroTelaio", telaioT.getText());
        if (labelSuTipologia!=null) modifiche.put(labelSuTipologia, ((TextField) vBoxAgg1.getChildren().get(1)).getText());
        modifiche.put("Posizione", posizioneCB.getValue());
        modifiche.put("Descrizione", descrizioneTA.getText());
        modifiche.put("Prezzo", prezzoTF.getText());
        modifiche.put("DataSegnalazione", data.getValue().toString());
        modifiche.put("StatoRiparazione", statoRipCB.getValue());
        database.updateUnitaVeicolo(modifiche);
    }
}

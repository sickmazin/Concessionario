package com.project.concessionario;

import com.project.concessionario.Prodotti.UnitaVeicolo;
import com.project.concessionario.SQL.MyJDBC;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.time.LocalDate;
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
        if (veicolo.getDataSegnalazione()!=null)        data.setValue(LocalDate.parse(veicolo.getDataSegnalazione()));
        posizioneCB.setValue(veicolo.getPosizione());
        marcaT.setText(veicolo.getMarca());
        carburanteT.setText(veicolo.getCarburante());
        descrizioneTA.setText(veicolo.getDescrizione());
        prezzoTF.setText(veicolo.getPrezzo()+"");

        if (veicolo.getTipologia()==null) return;
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
        modifiche.put("Tipologia",tipologiaT.getText());
        modifiche.put("Prezzo", prezzoTF.getText());
        if (data.getValue() !=null) {
            modifiche.put("DataSegnalazione", data.getValue().toString());
        }
        modifiche.put("StatoRiparazione", statoRipCB.getValue());
        chiudi(event);
        database.updateUnitaVeicolo(modifiche);
    }


    @FXML
    void chiudi(MouseEvent event) {
        try {
            Robot r=new Robot();
            r.keyPress(KeyCode.ESCAPE);
            r.keyRelease(KeyCode.ESCAPE);
        } catch(Exception e) {
            ErrorAlert er=new ErrorAlert("errore");
            er.show();
        }
    }
}

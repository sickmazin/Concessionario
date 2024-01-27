package com.project.concessionario;

import com.project.concessionario.Prodotti.UnitaVeicolo;
import com.project.concessionario.SQL.MyJDBC;
import com.project.concessionario.sidebarState.HideState;
import com.project.concessionario.sidebarState.TransitionState;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class VeicoloController implements Initializable {
    @FXML
    private TextField agg1;
    @FXML
    private TextField agg2;
    @FXML
    private TextField agg3;
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
    private ChoiceBox<String> modelloCB;
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
    @FXML
    private Label ordini;
    @FXML
    private Label fornitori;
    @FXML
    private Label clienti;
    @FXML
    private Label veicoli;
    @FXML
    private Label accessori;
    @FXML
    private Label appuntamenti;

    @FXML
    private TableView<UnitaVeicolo> tableView;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaCarburante;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaDescrizione;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaMarca;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaModello;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaPosizione;
    @FXML
    private TableColumn<UnitaVeicolo,Integer> colonnaPrezzo;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaSelezione;
    @FXML
    private TableColumn<UnitaVeicolo,String> colonnaTelaio;
    @FXML
    private ChoiceBox<String> tipologiaCB;
    private ObservableList<UnitaVeicolo> unitaVeicoloObservableList= FXCollections.observableArrayList();
    private TransitionState ts;
    private String tipo;
    private MyJDBC database = null;
    private ErrorAlert errorAlert;
    private HashMap<String, ChoiceBox<String>> corrispondenze = new HashMap<>();
    private ArrayList<String>  selezionati = new ArrayList<>();

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
    public void setTipo(String tipo) {
        switch (tipo) {
            case "Admin" -> { }
            case "Auto" -> gestioneAuto();
            case "Vendita" -> gestioneVendite();
            case "Noleggio" -> gestioneNoleggio();
            case "Contabile" -> gestioneContabile();
            case "Assistenza" -> gestioneAssistenza();
            case "Magazziniere" -> gestioneMagazzino();
            default -> {
                throw new IllegalArgumentException("QUALCOSA NON VA");
            }
        }
    }

    private void gestioneMagazzino() {
        clienti.setDisable(true);
        fornitori.setDisable(true);
        appuntamenti.setDisable(true);
    }

    private void gestioneAssistenza() {
        fornitori.setDisable(true);
        veicoli.setDisable(true);
        accessori.setDisable(true);
        ordini.setDisable(true);
    }

    private void gestioneContabile() {
        fornitori.setDisable(true);
        veicoli.setDisable(true);
        accessori.setDisable(true);
        clienti.setDisable(true);
        appuntamenti.setDisable(true);
    }

    private void gestioneNoleggio() {
        fornitori.setDisable(true);
        accessori.setDisable(true);
        ordini.setDisable(true);
    }

    private void gestioneVendite() {
        fornitori.setDisable(true);
        ordini.setDisable(true);
    }

    private void gestioneAuto() {
        accessori.setDisable(true);
        clienti.setDisable(true);
        appuntamenti.setDisable(true);
    }

    @FXML
    private void visualizza(MouseEvent event) {
        unitaVeicoloObservableList.clear();
        if (sceltaFiltroCB.getValue()==null) unitaVeicoloObservableList.addAll(database.getUnitaVeicolo("",""));
        else unitaVeicoloObservableList.addAll(database.getUnitaVeicolo(sceltaFiltroCB.getValue(), possibilitaCB.getValue()));
        tableView.refresh();
    }
    @FXML
    private void inserisci(MouseEvent event) {
        String[] nuovoVeicolo = new String[] { telaioTF.getText(),
                                               modelloCB.getValue(),
                                               posizioneCB.getValue(),
                                               descrizioneTF.getText(),
                                               carburanteCB.getValue(),
                                               prezzoTF.getText(),
                                               marcaCB.getValue() };
        database.insertUnitaVeicolo(nuovoVeicolo, tipologiaCB.getValue(), new ArrayList<>());
        visualizza(event);
    }
    @FXML
    private void elimina(MouseEvent event) {
        ArrayList<UnitaVeicolo> veicolos= getVeicoliSelezionati();
        database.deleteUnitaVeicolo(veicolos);
        tableView.refresh();
        visualizza(event);
    }

    private ArrayList<UnitaVeicolo> getVeicoliSelezionati() {
        ArrayList<UnitaVeicolo> veicolos= new ArrayList<>();
        for (UnitaVeicolo v: unitaVeicoloObservableList){
            if(v.getCheckBox().isSelected()) veicolos.add(v);
        }
        return veicolos;
    }

    @FXML
    private void modifica(MouseEvent event) {
        System.out.println("modifica");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ts=new HideState();
        ts.setNode(menu);
        ts.setTransition(new TranslateTransition(Duration.seconds(0.35),menu));
        ts.setFrom(0);
        ts.setTo(-134);

        ts.show();

        corrispondenze.put("Marca", marcaCB);
        corrispondenze.put("Modello", modelloCB);
        corrispondenze.put("Carburante", carburanteCB);
        corrispondenze.put("Tipologia", tipologiaCB);

        ChangeListener<String> filtroChangeListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldS, String newS) {
                possibilitaCB.getItems().clear();
                for (String item : corrispondenze.get(newS).getItems())
                    possibilitaCB.getItems().add(item);
            }
        };
        sceltaFiltroCB.getSelectionModel().selectedItemProperty().addListener(filtroChangeListener);

        ChangeListener<String> tipologiaChangeListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldS, String newS) {
                switch (newS) {
                    case "Usata" -> {
                        agg1.setVisible(true); agg2.setVisible(false); agg3.setVisible(false);
                    }
                    case "Auto da riparare" -> {
                        agg1.setVisible(true); agg2.setVisible(true); agg3.setVisible(true);
                    }
                    default -> {
                        agg1.setVisible(false); agg2.setVisible(false); agg3.setVisible(false);
                    }
                }
            }
        };
        tipologiaCB.getSelectionModel().selectedItemProperty().addListener(tipologiaChangeListener);

        try {
            database =  new MyJDBC();
        } catch(SQLException e) {
            errorAlert = new ErrorAlert(ErrorAlert.TYPE.SQL_EXCEPTION);
            errorAlert.show();
        }

        if (database!=null) {
            for (String carburante : database.getCARBURANTI()) carburanteCB.getItems().add(carburante);
            for (String tipologia : database.getTIPOLOGIA_VEICOLI()) tipologiaCB.getItems().add(tipologia);
            for (String posizione : database.getPOSIZIONI()) posizioneCB.getItems().add(posizione);
            for (String marca : database.getMARCA()) marcaCB.getItems().add(marca);
            for (String modello : database.getMODELLI()) modelloCB.getItems().add(modello);
        }


        sceltaFiltroCB.getItems().add("Marca");
        sceltaFiltroCB.getItems().add("Modello");
        sceltaFiltroCB.getItems().add("Carburante");
        sceltaFiltroCB.getItems().add("Tipologia");

        ordinaCB.getItems().add("Telaio");
        ordinaCB.getItems().add("Marca");
        ordinaCB.getItems().add("Modello");
        ordinaCB.getItems().add("Prezzo");
        ordinaCB.getItems().add("Tipologia");


        //TABLE
        colonnaDescrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        colonnaCarburante.setCellValueFactory(new PropertyValueFactory<>("carburante"));
        colonnaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colonnaModello.setCellValueFactory(new PropertyValueFactory<>("modello"));
        colonnaPosizione.setCellValueFactory(new PropertyValueFactory<>("posizione"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        colonnaSelezione.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        colonnaTelaio.setCellValueFactory(new PropertyValueFactory<>("numeroTelaio"));
        tableView.setItems(unitaVeicoloObservableList);


    }
}
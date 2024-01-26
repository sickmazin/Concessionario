package com.project.concessionario.SQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyJDBC extends DatabaseConnection {

    public MyJDBC() throws SQLException {
        super();
        CARBURANTI= new ArrayList<>(List.of(new String[]{"Benzina", "Diesel", "GPL", "Elettrica", "Hybrid"}));
        MODELLI= new ArrayList<>(modelliVeicolo());
        MARCA= new ArrayList<>(marcheVeicolo());
        TIPOLOGIA_VEICOLI= new ArrayList<>(List.of(new String[]{"Noleggiabile", "Nuova", "Usato", "Veicolo da riparare", "Altra Filiale"}));
        POSIZIONI=new ArrayList<>(List.of(new String[]{"Salone", "Parcheggio"}));
        STATO_RIPARAZIONE=new ArrayList<>(List.of(new String[]{"Da riparare", "In riparazione", "Riparata"}));
    }

    public Collection<String> getCARBURANTI() {
        return Collections.unmodifiableCollection(CARBURANTI);
    }
    public Collection<String> getTIPOLOGIA_VEICOLI() {
        return Collections.unmodifiableCollection(TIPOLOGIA_VEICOLI);
    }
    public Collection<String> getMODELLI() {
        return Collections.unmodifiableCollection(MODELLI);
    }
    public Collection<String> getMARCA() {
        return Collections.unmodifiableCollection(MARCA);
    }
    public Collection<String> getPOSIZIONI() {
        return Collections.unmodifiableCollection(POSIZIONI);
    }
    private final ArrayList<String> POSIZIONI;
    private final ArrayList<String> CARBURANTI;
    private final ArrayList<String> TIPOLOGIA_VEICOLI;
    private final ArrayList<String> STATO_RIPARAZIONE;
    private final ArrayList<String> MODELLI;
    private final ArrayList<String> MARCA;

    /**
     * Funziona utilizzata per ottenere i veicoli dal database.
     * @param scelta1 può assumere i seguenti valori: {"Marca","Modello", "Carburante","Tipologia" }, per un valore differente si ottengono tutti i veicoli.
     * @param scelta2 invece viene utilizzato a seconda della scelta1, per la ricerca vera e propria nella query.
     */
    public void getUnitaVeicolo(String scelta1, String scelta2){
        try {
            switch (scelta1){
                case "Marca" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'Marca'="+scelta2+" ;");
                case "Modello" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'modello'="+scelta2+" ;");
                case "Carburante" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'carburante'="+scelta2+" ;");
                case "Tipologia" -> {
                    switch (scelta2) {
                        case "Noleggiabile" -> statement.executeQuery("SELECT * FROM `concessionario`.`veicolo_noleggiabile`;");
                        case "Nuova" -> statement.executeQuery("SELECT * FROM `concessionario`.`veicolo_nuovo`;");
                        case "Usato" -> statement.executeQuery("SELECT * FROM `concessionario`.`veicolo_usato`;");
                        case "Veicolo da riparare" -> statement.executeQuery("SELECT * FROM `concessionario`.`veicolo_dariparare`;");
                        case "Altra Filiale" -> statement.executeQuery("SELECT * FROM `concessionario`.`veicoloaltrafiliale`;");
                    }
                }
                default -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo`;");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Permette di inserire l'unità veicolo che l'utente ha inserito nella gui, tramite le apposite parti. Esegue dei check sulle chiavi esterne, e sull' Enum prima di effettuare l'inserimento sul database.
     * @param query un array che viene definito in questo modo:
     *             query [0] -> NumeroTelaio,
     *             query [1] -> modello,
     *             query [2] -> posizione,
     *             query [3] -> descrizione,
     *             query [4] -> carburante,
     *             query [5] -> prezzo,
     *             query [6] -> Marca
     * @param tipologia indica la tipologia di veicolo, utilizzato per inserire nell'apposita table.
     * @param attrAggiuntivi array utilizzato per ottenere i valori degli attributi aggiuntivi delle tipologie USATO e DA RIPARARE
     */
    public void insertUnitaVeicolo(String[] query,String tipologia,ArrayList<String> attrAggiuntivi){
        try {
            String numeroTelaio= query[0];
            String modello= query[1];           if (!MODELLI.contains(modello)) throw new IllegalArgumentException("Modello inserito non presente nella lista Veicoli");
            String posizione=query [2];         if(!POSIZIONI.contains(posizione)) throw new IllegalArgumentException("Posizione errata");
            String descrizione= query [3];      if (descrizione.isBlank() || descrizione.isEmpty()) descrizione="NULL";
            String carburante= query [4];       if(!CARBURANTI.contains(carburante)) throw new IllegalArgumentException("Carburante  non presente nella lista");
            String marca=query [6];             if (!MARCA.contains(marca)) throw new IllegalArgumentException("Marca inserita non presente nella lista Veicoli");
            int prezzo= Integer.parseInt(query[5]);
            statement.executeUpdate("INSERT INTO `concessionario`.`unitàveicolo` VALUES (  '" +numeroTelaio+"', '"+modello+"','"+posizione+"','" +descrizione+"','" +carburante+"', '"+prezzo+"', '"+marca+"');");
            switch (tipologia) {
                case "Noleggiabile" ->        statement.executeQuery("INSERT INTO `concessionario`.`veicolo_noleggiabile` VALUES ('" +numeroTelaio+"');");
                case "Nuova" ->               statement.executeQuery("INSERT INTO `concessionario`.`veicolo_nuovo` VALUES ('" +numeroTelaio+"');");
                case "Usato" -> {
                    float chilometraggio;
                    try{
                        chilometraggio = Float.parseFloat(attrAggiuntivi.get(0));
                    }catch (NumberFormatException e){
                        throw new IllegalArgumentException("Valore del chilometraggio inserito sbagliato.");
                    }
                    statement.executeQuery("INSERT INTO `concessionario`.`veicolo_usato` VALUES ('" + numeroTelaio + "', '" + chilometraggio + "');");
                }
                case "Veicolo da riparare" -> {
                    String descrizioneDanno=attrAggiuntivi.get(0);
                    String dataSegnalazione=attrAggiuntivi.get(1); if (!dataSegnalazione.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException("DATA ERRATA");
                    String statoRiparazione=attrAggiuntivi.get(2); if (!STATO_RIPARAZIONE.contains(statoRiparazione)) throw new IllegalArgumentException("STATO RIPARAZIONE ERRATO");
                    statement.executeQuery("INSERT INTO `concessionario`.`veicolo_usato` VALUES ('" + numeroTelaio + "', '" + descrizioneDanno + "', '" + dataSegnalazione + "', '" + statoRiparazione + "');");
                }
                case "Altra Filiale" ->       statement.executeQuery("INSERT INTO `concessionario`.`veicoloaltrafiliale` VALUES ('" +numeroTelaio+"');");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Il prezzo passato non è un intero");
        }


    }
    private ArrayList<String> marcheVeicolo() {
        ArrayList<String> ret= new ArrayList<>();
        try {
            statement.executeQuery("SELECT `veicolo`.`Marca` FROM `concessionario`.`veicolo`;");
            ResultSet resultSet= statement.getResultSet();
            while (resultSet.next()){
                ret.add(resultSet.getString("Marca"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
    private ArrayList<String> modelliVeicolo() {
        ArrayList<String> ret= new ArrayList<>();
        try {
            statement.executeQuery("SELECT `veicolo`.`Modello` FROM `concessionario`.`veicolo`;");
            ResultSet resultSet= statement.getResultSet();
            while (resultSet.next()){
                ret.add(resultSet.getString("Modello"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public static void main(String[] args) {
        //MyJDBC jdbc=new MyJDBC();
        //jdbc.connectToDataBase();
        //String[] arr= {"jdsab344as","Modello","Parcheggio","ciaoo","Benzina","54353","Mercedes"};
        ////jdbc.insertUnitaVeicolo(arr);
        //System.out.print(jdbc.marcheVeicolo());
    }
}

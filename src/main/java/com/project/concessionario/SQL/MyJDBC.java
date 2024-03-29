package com.project.concessionario.SQL;
import com.project.concessionario.Prodotti.UnitaVeicolo;

import java.sql.*;
import java.util.*;

public class MyJDBC extends DatabaseConnection{
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
    public ArrayList<String> getPOSIZIONI() {
        return POSIZIONI;
    }

    public ArrayList<String> getSTATO_RIPARAZIONE() {
        return STATO_RIPARAZIONE;
    }

    private final ArrayList<String> POSIZIONI;
    private final ArrayList<String> CARBURANTI;
    private final ArrayList<String> TIPOLOGIA_VEICOLI;
    private final ArrayList<String> STATO_RIPARAZIONE;
    private final ArrayList<String> MODELLI;
    private final ArrayList<String> MARCA;

    /**
     * Funziona utilizzata per ottenere i veicoli dal database.
     *
     * @param scelta1 può assumere i seguenti valori: {"Marca","Modello", "Carburante","Tipologia" }, per un valore differente si ottengono tutti i veicoli.
     * @param scelta2 invece viene utilizzato a seconda della scelta1, per la ricerca vera e propria nella query.
     * @return restituisce una lista di UNITAVEICOLO che a seconda della scelta 2 avrà i relativi parametri aggiuntivi
     */
    public ArrayList<UnitaVeicolo> getUnitaVeicolo(String scelta1, String scelta2){
        ArrayList<UnitaVeicolo> veicoli= new ArrayList<>();
        try {
            switch (scelta1){
                case "Marca" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE Marca='"+scelta2+"' ;");
                case "Modello" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE Modello='"+scelta2+"' ;");
                case "Carburante" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE Carburante='"+scelta2+"' ;");
                case "Tipologia" -> {
                    switch (scelta2) {
                        case "Noleggiabile" ->        statement.executeQuery("SELECT * FROM veicolo_noleggiabile, unitàveicolo where veicolo_noleggiabile.Veicolo=unitàveicolo.NumeroTelaio;");
                        case "Nuova" ->               statement.executeQuery("SELECT * FROM veicolo_nuovo, unitàveicolo where veicolo_nuovo.Veicolo=unitàveicolo.NumeroTelaio;");
                        case "Usato" ->               statement.executeQuery("SELECT * FROM veicolo_usato, unitàveicolo where veicolo_usato.Veicolo=unitàveicolo.NumeroTelaio;");
                        case "Veicolo da riparare" -> statement.executeQuery("SELECT * FROM veicolo_dariparare, unitàveicolo where veicolo_dariparare.Veicolo=unitàveicolo.NumeroTelaio;");
                        case "Altra Filiale" ->       statement.executeQuery("SELECT * FROM veicoloaltrafiliale, unitàveicolo where veicoloaltrafiliale.Veicolo=unitàveicolo.NumeroTelaio;");
                    }
                }
                default -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo`;");
            }
            ResultSet resultSet= statement.getResultSet();
            while (resultSet.next()) {
                UnitaVeicolo veicolo;
                UnitaVeicolo.UnitaVeicoloBuilder builder = UnitaVeicolo.UnitaVeicoloBuilder.anUnitaVeicolo()
                        .withNumeroTelaio(resultSet.getString("NumeroTelaio"))
                        .withModello(resultSet.getString("Modello"))
                        .withPosizione(resultSet.getString("Posizione"))
                        .withDescrizione(resultSet.getString("Descrizione"))
                        .withCarburante(resultSet.getString("Carburante"))
                        .withPrezzo(resultSet.getInt("Prezzo"))
                        .withMarca(resultSet.getString("Marca"));
                switch (scelta2){
                    case "Usato" -> veicolo= builder.withChilometraggio(resultSet.getFloat("Chilometraggio"))
                                .build();

                    case "Veicolo da riparare" -> veicolo= builder.withDataSegnalazione(resultSet.getDate("DataSegnalazione").toString())
                                .withDescrizioneDanno(resultSet.getString("Descrizione_Danni"))
                                .withStatoRiparazione(resultSet.getString("StatoRiparazione"))
                                .build();

                    default -> veicolo = builder.build();

                }
                veicoli.add(veicolo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return veicoli;
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
    public void insertUnitaVeicolo(String[] query,String tipologia,ArrayList<String> attrAggiuntivi) throws SQLException, IllegalArgumentException, PrimaryKeyException {
        String numeroTelaio= query[0];
        String modello= query[1];           if (!MODELLI.contains(modello))                                           throw new IllegalArgumentException("Modello inserito non presente nella lista Veicoli");
        String posizione=query [2];         if(!POSIZIONI.contains(posizione))                                        throw new IllegalArgumentException("Posizione errata");
        String descrizione= query [3];      if (descrizione.isBlank()) descrizione="NULL";
        String carburante= query [4];       if(!CARBURANTI.contains(carburante))                                      throw new IllegalArgumentException("Carburante  non presente nella lista");
        String marca=query [6];             if (!MARCA.contains(marca))                                               throw new IllegalArgumentException("Marca inserita non presente nella lista Veicoli");
        int prezzo= Integer.parseInt(query[5]);

        //CHECK PRIMARY KEY
        statement.executeQuery("SELECT * FROM unitàveicolo WHERE NumeroTelaio='"+numeroTelaio+"';");
        ResultSet resultSet=statement.getResultSet();
        if (resultSet.next()) throw new  PrimaryKeyException();



        statement.executeUpdate("INSERT INTO unitàveicolo VALUES ('" +numeroTelaio+"','"+modello+"','"+posizione+"','" +descrizione+"','" +carburante+"','"+prezzo+"','"+marca+"');");
        switch (tipologia) {
            case "Noleggiabile" ->        statement.executeUpdate("INSERT INTO `concessionario`.`veicolo_noleggiabile` VALUES ('" +numeroTelaio+"');");
            case "Nuova" ->               statement.executeUpdate("INSERT INTO `concessionario`.`veicolo_nuovo` VALUES ('" +numeroTelaio+"');");
            case "Usato" -> {
                float chilometraggio;
                try{
                    chilometraggio = Float.parseFloat(attrAggiuntivi.get(0));
                }catch (NumberFormatException e){
                    throw new IllegalArgumentException("Valore del chilometraggio inserito sbagliato.");
                }
                statement.executeUpdate("INSERT INTO `concessionario`.`veicolo_usato` VALUES ('" + numeroTelaio + "', '" + chilometraggio + "');");
            }
            case "Veicolo da riparare" -> {
                String descrizioneDanno=attrAggiuntivi.get(0);
                String dataSegnalazione=attrAggiuntivi.get(1); if (!dataSegnalazione.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException("DATA ERRATA");
                String statoRiparazione=attrAggiuntivi.get(2); if (!STATO_RIPARAZIONE.contains(statoRiparazione)) throw new IllegalArgumentException("STATO RIPARAZIONE ERRATO");
                statement.executeUpdate("INSERT INTO `concessionario`.`veicolo_dariparare` VALUES ('" + numeroTelaio + "', '" + descrizioneDanno + "', '" + dataSegnalazione + "', '" + statoRiparazione + "');");
            }
            case "Altra Filiale" ->       statement.executeUpdate("INSERT INTO `concessionario`.`veicoloaltrafiliale` VALUES ('" +numeroTelaio+"');");
        }
    }


    public UnitaVeicolo getUnitaVeicoloDaModificare(UnitaVeicolo veicolo){
        ResultSet resultSet;
        UnitaVeicolo.UnitaVeicoloBuilder builder=UnitaVeicolo.UnitaVeicoloBuilder.anUnitaVeicolo()
                .withNumeroTelaio(veicolo.getNumeroTelaio())
                .withModello(veicolo.getModello())
                .withPosizione(veicolo.getPosizione())
                .withDescrizione(veicolo.getDescrizione())
                .withCarburante(veicolo.getCarburante())
                .withPrezzo(veicolo.getPrezzo())
                .withMarca(veicolo.getMarca());
        try {
            statement.executeQuery("SELECT * FROM veicolo_noleggiabile WHERE Veicolo= '"+veicolo.getNumeroTelaio()+"';");
            resultSet=statement.getResultSet();
            if (resultSet.next()){
                //VEICOLO NOLEGGIABILE
                return builder.withTipologia("Noleggiabile").build();
            }else {
                statement.executeQuery("SELECT * FROM veicolo_nuovo where Veicolo= '"+veicolo.getNumeroTelaio()+"';");
                resultSet=statement.getResultSet();
                if (resultSet.next()){
                    //VEICOLO NUOVO
                    return builder.withTipologia("Nuova").build();
                }else {
                    statement.executeQuery("SELECT * FROM veicolo_usato where Veicolo= '"+veicolo.getNumeroTelaio()+"';");
                    resultSet=statement.getResultSet();
                    if (resultSet.next()){
                        //VEICOLO USATO
                        return builder.withTipologia("Usato").withChilometraggio(resultSet.getFloat("Chilometraggio")).build();
                    }else {
                        statement.executeQuery("SELECT * FROM veicolo_dariparare where Veicolo= '"+veicolo.getNumeroTelaio()+"';");
                        resultSet=statement.getResultSet();
                        if (resultSet.next()){
                            //VEICOLO DA RIPARARE
                            return builder.withDataSegnalazione(resultSet.getDate("DataSegnalazione").toString())
                                    .withDescrizioneDanno(resultSet.getString("Descrizione_Danni"))
                                    .withStatoRiparazione(resultSet.getString("StatoRiparazione"))
                                    .withTipologia("Veicolo da riparare")
                                    .build();
                        }else {
                            statement.executeQuery("SELECT * FROM veicoloaltrafiliale where Veicolo= '"+veicolo.getNumeroTelaio()+"';");
                            resultSet=statement.getResultSet();
                            resultSet.next();
                            return builder.withTipologia("Altra Filiale").build();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> marcheVeicolo() {
        ArrayList<String> ret= new ArrayList<>();
        try {
            statement.executeQuery("SELECT nome FROM `concessionario`.`marca`;");
            ResultSet resultSet= statement.getResultSet();
            while (resultSet.next()){
                ret.add(resultSet.getString("nome"));
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
    public void updateUnitaVeicolo(HashMap<String, String> map){
        /*
          KEY HASH MAP:
        * Posizione, Descrizione, Prezzo, NumeroTelaio
        * Chilometraggio, DescrizioneDanni, DataSegnalazione, StatoRiparazione
        * */
        try {
            statement.executeUpdate("UPDATE  unitàveicolo SET Posizione='"+map.get("Posizione")+"' , Descrizione='"+map.get("Descrizione")+"' , Prezzo='"+Integer.parseInt(map.get("Prezzo"))+"' WHERE NumeroTelaio='"+map.get("NumeroTelaio")+"';");
            if (map.get("Tipologia")!= null) {
                switch (map.get("Tipologia")) {
                    case "Veicolo da riparare" ->
                            statement.executeUpdate("UPDATE veicolo_dariparare SET Descrizione_Danni='"+ map.get("DescrizioneDanni")+"', DataSegnalazione='" + map.get("DataSegnalazione")+"', StatoRiparazione='" + map.get("StatoRiparazione")+"' WHERE Veicolo ='"+ map.get("NumeroTelaio") + "';");
                    case "Usato" ->
                            statement.executeUpdate("UPDATE veicolo_usato SET  Chilometraggio='"+ map.get("Chilometraggio")+"' WHERE Veicolo='"+ map.get("NumeroTelaio")+"';");
                    default -> {
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteUnitaVeicolo(ArrayList<UnitaVeicolo> veicolos) {
        try {
            for (UnitaVeicolo v:veicolos){
                statement.executeUpdate("DELETE FROM unitàveicolo WHERE NumeroTelaio='"+v.getNumeroTelaio()+"';");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

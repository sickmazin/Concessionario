package com.project.concessionario.SQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyJDBC {

    public MyJDBC() {
        connectToDataBase();
        CARBURANTI= new ArrayList<>(List.of(new String[]{"Benzina", "Diesel", "GPL", "Elettrica", "Hybrid"}));
        MODELLI= new ArrayList<>(modelliVeicolo());
        MARCA= new ArrayList<>(marcheVeicolo());
        TIPOLOGIA_VEICOLI= new ArrayList<>(List.of(new String[]{"Noleggiabile", "Nuova", "Usato", "Veicolo da riparare", "Altra Filiale"}));
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

    private static Statement statement;
    private ArrayList<String> CARBURANTI;
    private ArrayList<String> TIPOLOGIA_VEICOLI;
    private ArrayList<String> MODELLI;
    private ArrayList<String> MARCA;
    public void connectToDataBase(){
        Connection connection= null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/concessionario",
                    "root",
                    "mattia02"
            );
            statement = connection.createStatement();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUnitaVeicolo(String scelta1, String scelta2){
        try {
            switch (scelta1){
                case "Marca" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'Marca'="+scelta2+" ;");
                case "Modello" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'modello'="+scelta2+" ;");
                case "Carburante" -> statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo` WHERE `unitàveicolo`.'carburante'="+scelta2+" ;");
                case "Tipologia" -> {
                    switch (scelta2) {

                    }
                }
            }
            statement.executeQuery("SELECT * FROM `concessionario`.`unitàveicolo`;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertUnitaVeicolo(String[] query){
        /*  query [0] -> NumeroTelaio
            query [1] -> modello
            query [2] -> posizione
            query [3] -> descrizione
            query [4] -> carburante
            query [5] -> prezzo
            query [6] -> Marca
        * */
        String numeroTelaio= query[0];
        String modello= query[1];           //if (!MODELLI.contains(modello)) throw new IllegalArgumentException("Modello inserito non presente nella lista Veicoli");
        String posizione=query [2];         if(!posizione.equals("Parcheggio") && !posizione.equals("Salone")) throw new IllegalArgumentException("Posizione errata");
        String descrizione= query [3];      if (descrizione.isBlank()|| descrizione.isEmpty()) descrizione="NULL";
        String carburante= query [4];       if(!CARBURANTI.contains(carburante)) throw new IllegalArgumentException("Carburante  non presente nella lista");
        int prezzo;
        try {
            prezzo = Integer.parseInt(query[5]);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Il prezzo passato non è un intero");
        }
        String marca=query [6]; //if (!MARCA.contains(marca)) throw new IllegalArgumentException("Marca inserita non presente nella lista Veicoli");

        try {
            statement.executeUpdate("INSERT INTO `concessionario`.`unitàveicolo` VALUES (  '" +numeroTelaio+"', '"+modello+"','"+posizione+"','" +descrizione+"','" +carburante+"', '"+prezzo+"', '"+marca+"');");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        MyJDBC jdbc=new MyJDBC();
        jdbc.connectToDataBase();
        String[] arr= {"jdsab344as","Modello","Parcheggio","ciaoo","Benzina","54353","Mercedes"};
        //jdbc.insertUnitaVeicolo(arr);
        System.out.print(jdbc.marcheVeicolo());
    }
}

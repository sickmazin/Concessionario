package com.project.concessionario.Prodotti;

public class UnitaVeicolo {
    String numeroTelaio;    String modello;    String posizione;    String descrizione;    String carburante;    int prezzo;  String marca;

    @Override
    public String toString() {
        return "UnitaVeicolo{" +
                "numeroTelaio='" + numeroTelaio + '\'' +
                ", modello='" + modello + '\'' +
                ", posizione='" + posizione + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", carburante='" + carburante + '\'' +
                ", prezzo=" + prezzo +
                ", marca='" + marca + '\'' +
                ", descrizioneDanno='" + descrizioneDanno + '\'' +
                ", dataSegnalazione='" + dataSegnalazione + '\'' +
                ", statoRiparazione='" + statoRiparazione + '\'' +
                ", chilometraggio=" + chilometraggio +
                '}';
    }

    //PARAMETRI OPZIONALI
    String descrizioneDanno; String dataSegnalazione; String statoRiparazione;  float chilometraggio;
    private UnitaVeicolo() {
    }
    public static final class UnitaVeicoloBuilder {
        private String numeroTelaio;        private String modello;        private String posizione;        private String descrizione;        private String carburante;        private int prezzo;        private String marca;        private String descrizioneDanno;        private  String dataSegnalazione;        private String statoRiparazione;        private  float chilometraggio;
        private UnitaVeicoloBuilder() {
        }

        public static UnitaVeicoloBuilder anUnitaVeicolo() {
            return new UnitaVeicoloBuilder();
        }

        public UnitaVeicoloBuilder withNumeroTelaio(String numeroTelaio) {
            this.numeroTelaio = numeroTelaio;
            return this;
        }

        public UnitaVeicoloBuilder withModello(String modello) {
            this.modello = modello;
            return this;
        }

        public UnitaVeicoloBuilder withPosizione(String posizion) {
            this.posizione = posizion;
            return this;
        }

        public UnitaVeicoloBuilder withDescrizione(String descrizione) {
            this.descrizione = descrizione;
            return this;
        }

        public UnitaVeicoloBuilder withCarburante(String carburante) {
            this.carburante = carburante;
            return this;
        }

        public UnitaVeicoloBuilder withPrezzo(int prezzo) {
            this.prezzo = prezzo;
            return this;
        }

        public UnitaVeicoloBuilder withMarca(String marca) {
            this.marca = marca;
            return this;
        }
        public UnitaVeicoloBuilder withDescrizioneDanno(String descrizioneDanno) {
            this.descrizioneDanno = descrizioneDanno;
            return this;
        }
        public UnitaVeicoloBuilder withDataSegnalazione(String dataSegnalazione) {
            this.dataSegnalazione = dataSegnalazione;
            return this;
        }

        public UnitaVeicoloBuilder withChilometraggio(float chilometraggio) {
            this.chilometraggio = chilometraggio;
            return this;
        }
        public UnitaVeicoloBuilder withStatoRiparazione(String statoRiparazione) {
            this.statoRiparazione = statoRiparazione;
            return this;
        }

        public UnitaVeicolo build() {
            UnitaVeicolo unitaVeicolo = new UnitaVeicolo();
            unitaVeicolo.numeroTelaio = this.numeroTelaio;
            unitaVeicolo.posizione = this.posizione;
            unitaVeicolo.prezzo = this.prezzo;
            unitaVeicolo.descrizioneDanno=this.descrizioneDanno;
            unitaVeicolo.descrizione = this.descrizione;
            unitaVeicolo.dataSegnalazione=this.dataSegnalazione;
            unitaVeicolo.marca = this.marca;
            unitaVeicolo.modello = this.modello;
            unitaVeicolo.carburante = this.carburante;
            unitaVeicolo.chilometraggio=this.chilometraggio;
            unitaVeicolo.statoRiparazione=this.statoRiparazione;
            return unitaVeicolo;
        }
    }
}

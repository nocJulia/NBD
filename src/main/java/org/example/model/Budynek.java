package org.example.model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;

import java.util.ArrayList;
import java.util.List;

public class Budynek extends AbstractEntityMgd {

    @BsonProperty("nazwa")
    private String nazwa;

    @BsonProperty("lokale")
    private List<Lokal> lokale;

    @BsonCreator
    public Budynek(UniqueIdMgd entityId, @BsonProperty("nazwa") String nazwa) {
        super(entityId);  // Wywołanie konstruktora z klasy bazowej
        this.nazwa = nazwa;
        this.lokale = new ArrayList<>();
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Lokal> getLokale() {
        return lokale;
    }

    public void setLokale(List<Lokal> lokale) {
        this.lokale = lokale;
    }

    // Add a Lokal to the building
    public void dodajLokal(Lokal lokal) {
        if (lokal != null) {
            lokale.add(lokal);
        }
    }

    // Calculate total rent (czynsz)
    public double czynszCalkowity() {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz();
        }
        return suma;
    }

    // Calculate profit (zysk) based on expenses per square meter
    public double zysk(double wydatkiZaMetr) {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz() - (lokal.dajPowierzchnie() * wydatkiZaMetr);
        }
        return Math.max(suma, 0); // Ensure non-negative profit
    }

    // Get a summary of all local properties
    public String informacjaZbiorcza() {
        StringBuilder info = new StringBuilder();
        for (Lokal lokal : lokale) {
            info.append(lokal.informacja()).append("\n");
        }
        return info.toString();
    }
}

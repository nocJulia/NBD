package org.example.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

public abstract class Lokal extends AbstractEntityMgd {

    public Lokal(UniqueIdMgd entityId, double powierzchnia, double stawka) {
        super(entityId);  // Wywołanie konstruktora z klasy bazowej
        this.powierzchnia_w_metrach = powierzchnia;
        this.stawka = stawka;
    }

    @BsonProperty("budynek")
    private Budynek budynek; // Assume Budynek is also mapped with MongoDB annotations

    @BsonProperty("powierzchnia_w_metrach")
    private double powierzchnia_w_metrach;

    @BsonProperty("stawka")
    private double stawka;

    // Versioning can be handled in MongoDB with special fields like "_version" if needed
    @BsonProperty("version")
    private Long version; // MongoDB does not have native support for optimistic locking

    public Budynek getBudynek() {
        return budynek;
    }

    public void setBudynek(Budynek budynek) {
        this.budynek = budynek;
    }

    @BsonProperty("powierzchnia_w_metrach")
    public double dajPowierzchnie() {
        return powierzchnia_w_metrach;
    }

    @BsonProperty("stawka")
    public double dajStawke() {
        return stawka;
    }

    public void ustawPowierzchnie(double powierzchnia) {
        if (powierzchnia > 0) {
            this.powierzchnia_w_metrach = powierzchnia;
        }
    }

    public void ustawStawke(double stawka) {
        if (stawka > 0) {
            this.stawka = stawka;
        }
    }

    // Abstract method for subclasses to implement
    public abstract double czynsz();

    public String informacja() {
        return "Powierzchnia w metrach: " + powierzchnia_w_metrach + " Stawka: " + stawka;
    }
}

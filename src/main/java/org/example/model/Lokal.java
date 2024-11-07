package org.example.model;

import org.bson.types.ObjectId;

public abstract class Lokal {

    public ObjectId _id;

    public Lokal(ObjectId _id, Budynek budynek, double powierzchnia_w_metrach, double stawka) {
        this._id = _id;
        this.budynek = budynek;
        this.powierzchnia_w_metrach = powierzchnia_w_metrach;
        this.stawka = stawka;
    }

    private Budynek budynek;

    private double powierzchnia_w_metrach;

    private double stawka;

    public ObjectId get_id() {
        return _id;
    }

    public Budynek getBudynek() {
        return budynek;
    }

    public void setBudynek(Budynek budynek) {
        this.budynek = budynek;
    }

    public double dajPowierzchnie() {
        return powierzchnia_w_metrach;
    }

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

    public abstract double czynsz();

    public String informacja() {
        return "Powierzchnia w metrach: " + powierzchnia_w_metrach + " Stawka: " + stawka;
    }
}

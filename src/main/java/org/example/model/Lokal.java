package org.example.model;

public abstract class Lokal {
    private double powierzchnia_w_metrach;
    private double stawka;

    public Lokal(double powierzchnia, double stawka) {
        this.powierzchnia_w_metrach = powierzchnia;
        this.stawka = stawka;
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


package org.example;

import java.util.ArrayList;
import java.util.List;

public class Budynek {
    private List<Lokal> lokale;

    public Budynek() {
        this.lokale = new ArrayList<>();
    }

    public void dodajLokal(Lokal lokal) {
        if (lokal != null) {
            lokale.add(lokal);
        }
    }

    public double czynszCalkowity() {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz();
        }
        return suma;
    }

    public double zysk(double wydatkiZaMetr) {
        double suma = 0;
        for (Lokal lokal : lokale) {
            suma += lokal.czynsz() - (lokal.dajPowierzchnie() * wydatkiZaMetr);
        }
        return Math.max(suma, 0);
    }

    public String informacjaZbiorcza() {
        StringBuilder info = new StringBuilder();
        for (Lokal lokal : lokale) {
            info.append(lokal.informacja()).append("\n");
        }
        return info.toString();
    }
}


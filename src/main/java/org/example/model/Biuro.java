package org.example.model;

import org.bson.types.ObjectId;

public class Biuro extends Lokal {

    private double kosztyDodatkowe;

    public Biuro(ObjectId _id, Budynek budynek, double powierzchnia_w_metrach, double stawka, double kosztyDodatkowe) {
        super(_id, budynek, powierzchnia_w_metrach, stawka);
        this.kosztyDodatkowe = kosztyDodatkowe;
    }

    @Override
    public double czynsz() {
        return (dajPowierzchnie() * dajStawke()) + dajKoszty();
    }

    public double dajKoszty() {
        return kosztyDodatkowe;
    }

    public void ustawKoszty(double koszty) {
        if (koszty > 0) {
            this.kosztyDodatkowe = koszty;
        }
    }

    @Override
    public String informacja() {
        return "[Biuro] " + super.informacja() + " Koszty dodatkowe: " + kosztyDodatkowe;
    }
}

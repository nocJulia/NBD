package org.example.model;

import jakarta.persistence.Entity;

@Entity
public class Biuro extends Lokal {

    private double kosztyDodatkowe;

    // Bezargumentowy konstruktor wymagany przez JPA
    public Biuro() {}

    public Biuro(double powierzchnia, double stawka, double kosztyDodatkowe) {
        super(powierzchnia, stawka);
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



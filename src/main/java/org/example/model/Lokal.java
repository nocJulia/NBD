package org.example.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Lokal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "budynek_id") // Kolumna z kluczem obcym wskazującym na budynek
    private Budynek budynek;

    public Budynek getBudynek() {
        return budynek;
    }

    public void setBudynek(Budynek budynek) {
        this.budynek = budynek;
    }

    private double powierzchnia_w_metrach;
    private double stawka;

    @Version // Blokada optymistyczna
    private Long version; // Pole, które trzyma wersję rekordu

    public Long getVersion() {
        return version;
    }

    // Bezargumentowy konstruktor wymagany przez JPA
    public Lokal() {}

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









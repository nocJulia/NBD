package org.example.model;

import jakarta.persistence.Entity;

@Entity
public class Mieszkanie extends Lokal {

    // Bezargumentowy konstruktor wymagany przez JPA
    public Mieszkanie() {}

    public Mieszkanie(double powierzchnia, double stawka) {

        super(powierzchnia, stawka);
    }

    @Override
    public double czynsz() {

        return dajPowierzchnie() * dajStawke();
    }

    @Override
    public String informacja() {

        return "[Mieszkanie] " + super.informacja();
    }
}


package org.example.model;

public class Mieszkanie extends Lokal {

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


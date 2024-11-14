package org.example.model;

import org.bson.types.ObjectId;

public class Mieszkanie extends Lokal {

    public Mieszkanie(Budynek budynek, double powierzchnia_w_metrach, double stawka) {
        super(budynek, powierzchnia_w_metrach, stawka);
    }

    public Mieszkanie(ObjectId _id, Budynek budynek, double powierzchnia_w_metrach, double stawka) {
        super(_id, budynek, powierzchnia_w_metrach, stawka);
    }

    @Override
    public double czynsz() {
        return dajPowierzchnie() * dajStawke(); // Calculate rent based on area and rate
    }

    @Override
    public String informacja() {
        return "[Mieszkanie] " + super.informacja();
    }
}

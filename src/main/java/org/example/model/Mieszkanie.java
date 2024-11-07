package org.example.model;

import org.bson.types.ObjectId;

public class Mieszkanie extends Lokal {

    public Mieszkanie(ObjectId _id, Budynek budynek, double powierzchnia_w_metrach, double stawka) {
        super(_id, budynek, powierzchnia_w_metrach, stawka);
    }

    // Implementing the czynsz (rent) method
    @Override
    public double czynsz() {
        return dajPowierzchnie() * dajStawke(); // Calculate rent based on area and rate
    }

    // Provide information specific to this class
    @Override
    public String informacja() {
        return "[Mieszkanie] " + super.informacja();
    }
}

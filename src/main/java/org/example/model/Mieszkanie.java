package org.example.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

// This class extends the Lokal class, which has already been refactored for MongoDB.
public class Mieszkanie extends Lokal {

    // Constructor with parameters
    public Mieszkanie(UniqueIdMgd entityId, @BsonProperty("powierzchnia") double powierzchnia,
                      @BsonProperty("stawka") double stawka) {
        super(entityId, powierzchnia, stawka);
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

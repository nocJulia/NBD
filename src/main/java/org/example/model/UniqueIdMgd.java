package org.example.model;

import java.util.UUID;

// Klasa, która reprezentuje unikalny identyfikator
public class UniqueIdMgd {

    private String id;

    // Konstruktor, który generuje losowy unikalny identyfikator
    public UniqueIdMgd() {
        this.id = UUID.randomUUID().toString();  // Generowanie unikalnego ID
    }

    // Konstruktor przyjmujący istniejący identyfikator
    public UniqueIdMgd(String id) {
        this.id = id;
    }

    // Getter dla identyfikatora
    public String getId() {
        return id;
    }

    // Setter dla identyfikatora
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}

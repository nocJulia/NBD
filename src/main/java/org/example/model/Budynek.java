package org.example.model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "buildings")  // Określenie przestrzeni nazw bazy danych
@CqlName("budynki")  // Określenie nazwy tabeli w Cassandra
public class Budynek {

    @PartitionKey
    @CqlName("id")  // Klucz partycji
    private UUID id;  // Unikalny identyfikator budynku

    @CqlName("nazwa")  // Nazwa budynku
    private String nazwa;  // Unikalny identyfikator budynku

    @CqlName("lokale")  // Kolumna przechowująca listę lokali jako JSON
    private String lokaleJson;  // Lista lokali zapisana w formacie JSON

    public Budynek() {
        // Konstruktor bez argumentów jest wymagany przez Cassandra
    }

    public Budynek(String nazwa) {
        this.id = UUID.randomUUID();  // Generowanie losowego UUID dla budynku
        this.nazwa = nazwa;
        this.lokaleJson = "[]";  // Pusta lista lokali w formacie JSON
    }

    public Budynek(UUID id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
        this.lokaleJson = "[]";  // Pusta lista lokali w formacie JSON
    }

    // Getter i Setter dla nazwy
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    // Getter i Setter dla id
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Getter i Setter dla listy lokali
    public List<Lokal> getLokale() {
        // Deserializacja JSON na listę obiektów Lokal
        Gson gson = new Gson();
        return gson.fromJson(lokaleJson, List.class);
    }

    public void setLokale(List<Lokal> lokale) {
        // Serializacja listy Lokali do JSON
        Gson gson = new Gson();
        this.lokaleJson = gson.toJson(lokale);
    }

    // Metoda do dodawania lokalu do budynku
    public void dodajLokal(Lokal lokal) {
        List<Lokal> lokale = getLokale();
        lokale.add(lokal);
        setLokale(lokale);  // Zaktualizowanie JSON-a
    }
}

package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity(defaultKeyspace = "buildings")  // Zmieniamy na odpowiednią nazwę keyspace
@CqlName("lokale")  // Ustawiamy nazwę tabeli w bazie danych
public class Lokal {

    @PartitionKey  // Klucz partycji, identyfikator lokalu
    @CqlName("id")
    private UUID id;

    @CqlName("powierzchnia_w_metrach")
    private double powierzchnia_w_metrach;

    @CqlName("stawka")
    private double stawka;

    @CqlName("typ")
    private String typ;  // Możesz dodać pole dla typu lokalu, np. mieszkanie, biuro

    public Lokal(UUID id, double powierzchnia, double stawka, String typ) {
        this.id = id;
        this.powierzchnia_w_metrach = powierzchnia;
        this.stawka = stawka;
        this.typ = typ;
    }

    public Lokal() {
        // Konstruktor domyślny, wymagany przez bibliotekę mappera
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPowierzchnia_w_metrach() {
        return powierzchnia_w_metrach;
    }

    public void setPowierzchnia_w_metrach(double powierzchnia_w_metrach) {
        if (powierzchnia_w_metrach > 0) {
            this.powierzchnia_w_metrach = powierzchnia_w_metrach;
        }
    }

    public double getStawka() {
        return stawka;
    }

    public void setStawka(double stawka) {
        if (stawka > 0) {
            this.stawka = stawka;
        }
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    // Metoda obliczająca czynsz, zależna od implementacji konkretnej klasy (np. Mieszkanie, Biuro)
    public double czynsz() {
        return 0;
    }

    // Metoda informacyjna
    public String informacja() {
        return "ID: " + id + " | Powierzchnia: " + powierzchnia_w_metrach + " m² | Stawka: " + stawka + " | Typ: " + typ;
    }
}
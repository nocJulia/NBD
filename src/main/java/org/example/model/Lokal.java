package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity(defaultKeyspace = "buildings")
@CqlName("lokale")
public class Lokal {

    @PartitionKey
    @CqlName("id")
    private UUID id;

    @CqlName("powierzchnia_w_metrach")
    private double powierzchnia_w_metrach;

    @CqlName("stawka")
    private double stawka;

    @CqlName("typ")
    private String typ;

    public Lokal(UUID id, double powierzchnia, double stawka, String typ) {
        this.id = id;
        this.powierzchnia_w_metrach = powierzchnia;
        this.stawka = stawka;
        this.typ = typ;
    }

    public Lokal() {
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

    public double czynsz() {
        return 0;
    }
    public String informacja() {
        return "ID: " + id + " | Powierzchnia: " + powierzchnia_w_metrach + " m² | Stawka: " + stawka + " | Typ: " + typ;
    }
}

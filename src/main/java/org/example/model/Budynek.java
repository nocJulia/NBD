package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Transient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "buildings")
@CqlName("budynki")
public class Budynek {
    @PartitionKey
    @CqlName("id")
    private UUID id;

    @CqlName("nazwa")
    private String nazwa;

    @CqlName("lokale")
    private String lokaleJson;

    @Transient
    private List<Lokal> lokale;

    public Budynek() {
        this.lokale = new ArrayList<>();
        this.lokaleJson = "[]";
    }

    public Budynek(String nazwa) {
        this.id = UUID.randomUUID();
        this.nazwa = nazwa;
        this.lokale = new ArrayList<>();
        this.lokaleJson = "[]";
    }

    public Budynek(UUID id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
        this.lokale = new ArrayList<>();
        this.lokaleJson = "[]";
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Lokal> getLokale() {
        if (lokaleJson != null && !lokaleJson.isEmpty()) {
            Gson gson = new Gson();
            lokale = gson.fromJson(lokaleJson, new TypeToken<List<Lokal>>() {}.getType());
        }
        return lokale;
    }

    public void setLokale(List<Lokal> lokale) {
        this.lokale = lokale;
        Gson gson = new Gson();
        this.lokaleJson = gson.toJson(lokale);
    }

    public void dodajLokal(Lokal lokal) {
        if (this.lokale == null) {
            this.lokale = new ArrayList<>();
        }
        this.lokale.add(lokal);
        Gson gson = new Gson();
        this.lokaleJson = gson.toJson(this.lokale);
    }

    public String getLokaleJson() {
        return lokaleJson;
    }

    public void setLokaleJson(String lokaleJson) {
        this.lokaleJson = lokaleJson;
        // Optionally, update lokale list when JSON is set
        if (lokaleJson != null && !lokaleJson.isEmpty()) {
            Gson gson = new Gson();
            this.lokale = gson.fromJson(lokaleJson, new TypeToken<List<Lokal>>() {}.getType());
        }
    }
}
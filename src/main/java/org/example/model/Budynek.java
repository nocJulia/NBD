//package org.example.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Budynek {
//    private List<Lokal> lokale;
//
//    public Budynek() {
//        this.lokale = new ArrayList<>();
//    }
//
//    public void dodajLokal(Lokal lokal) {
//        if (lokal != null) {
//            lokale.add(lokal);
//        }
//    }
//
//    public double czynszCalkowity() {
//        double suma = 0;
//        for (Lokal lokal : lokale) {
//            suma += lokal.czynsz();
//        }
//        return suma;
//    }
//
//    public double zysk(double wydatkiZaMetr) {
//        double suma = 0;
//        for (Lokal lokal : lokale) {
//            suma += lokal.czynsz() - (lokal.dajPowierzchnie() * wydatkiZaMetr);
//        }
//        return Math.max(suma, 0);
//    }
//
//    public String informacjaZbiorcza() {
//        StringBuilder info = new StringBuilder();
//        for (Lokal lokal : lokale) {
//            info.append(lokal.informacja()).append("\n");
//        }
//        return info.toString();
//    }
//}
//

package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.ids.CassandraIds;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = CassandraIds.KEYSPACE)
@CqlName(CassandraIds.BUDYNEK_TABLE)
public class Budynek extends AbstractEntity {

    @CqlName("lokale")
    private List<UUID> lokale;

    public Budynek() {
        super(UUID.randomUUID(), "Budynek");
        this.lokale = new ArrayList<>();
    }

    public void dodajLokal(UUID lokalId) {
        if (lokalId != null) {
            lokale.add(lokalId);
        }
    }

    public double czynszCalkowity() {
        // This could be a query fetching all the Lokale and summing up their czynsz amounts.
        double suma = 0;
        for (UUID lokalId : lokale) {
            // Logic to fetch Lokale by UUID and sum up the czynsz, assuming a getCzynszByLokalId method
            suma += LokaleService.getCzynszByLokalId(lokalId);
        }
        return suma;
    }

    public double zysk(double wydatkiZaMetr) {
        // Fetching Lokale and calculating the profit
        double suma = 0;
        for (UUID lokalId : lokale) {
            suma += LokaleService.getCzynszByLokalId(lokalId) - (LokaleService.getPowierzchniaByLokalId(lokalId) * wydatkiZaMetr);
        }
        return Math.max(suma, 0);
    }

    public String informacjaZbiorcza() {
        // Gather information for each local using UUIDs
        StringBuilder info = new StringBuilder();
        for (UUID lokalId : lokale) {
            // Assuming LokaleService has a method to get info by UUID
            info.append(LokaleService.getInformacjaByLokalId(lokalId)).append("\n");
        }
        return info.toString();
    }

    // Getters and Setters for 'lokale' if needed
    public List<UUID> getLokale() {
        return lokale;
    }

    public void setLokale(List<UUID> lokale) {
        this.lokale = lokale;
    }
}

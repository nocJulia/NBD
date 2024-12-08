//package org.example.model;
//
<<<<<<< HEAD
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
=======
//import com.datastax.oss.driver.api.mapper.annotations.CqlName;
//import com.datastax.oss.driver.api.mapper.annotations.Entity;
//import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Entity(defaultKeyspace = "buildings")  // Ustawienie przestrzeni nazw bazy danych
//@CqlName("budynki")  // Określenie nazwy tabeli w Cassandra
//public class Budynek {
//
//    @PartitionKey
//    @CqlName("id")  // Klucz partycji
//    private UUID id;  // Unikalny identyfikator budynku
//
//    @CqlName("nazwa")  // Klucz partycji
//    private String nazwa;  // Unikalny identyfikator budynku
//
//    private List<Lokal> lokale;  // Lista lokali w budynku
//
//    public Budynek() {
//        // Konstruktor bez argumentów jest wymagany przez Cassandra
//    }
//
//    public Budynek(String nazwa) {
//        this.id = UUID.randomUUID();  // Generowanie losowego UUID dla budynku
//        this.nazwa = nazwa;
//        this.lokale = new ArrayList<>();
//    }
//
//    public Budynek(UUID id, String nazwa) {
//        this.id = id;
//        this.nazwa = nazwa;
//        this.lokale = new ArrayList<>();
//    }
//
//    public String getNazwa() {
//        return nazwa;
//    }
//
//    public void setNazwa(String nazwa) {
//        this.nazwa = nazwa;
//    }
//
//    // Getter i Setter dla id
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    // Getter i Setter dla listy lokali
//    public List<Lokal> getLokale() {
//        return lokale;
//    }
//
//    public void setLokale(List<Lokal> lokale) {
//        this.lokale = lokale;
//    }
//
//    // Metoda do dodawania lokalu do budynku
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
//    public void dodajLokal(Lokal lokal) {
//        if (lokal != null) {
//            lokale.add(lokal);
//        }
//    }
//
<<<<<<< HEAD
//    public double czynszCalkowity() {
//        double suma = 0;
//        for (Lokal lokal : lokale) {
//            suma += lokal.czynsz();
=======
//    // Metoda do obliczenia całkowitego czynszu w budynku
//    public double czynszCalkowity() {
//        double suma = 0;
//        for (Lokal lokal : lokale) {
//            suma += lokal.czynsz();  // Zbiera czynsz ze wszystkich lokali
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
//        }
//        return suma;
//    }
//
<<<<<<< HEAD
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
=======
//    // Metoda do obliczenia zysku z budynku, uwzględniając wydatki za metr
//    public double zysk(double wydatkiZaMetr) {
//        double suma = 0;
//        for (Lokal lokal : lokale) {
//            suma += lokal.czynsz() - (lokal.getPowierzchnia_w_metrach() * wydatkiZaMetr);  // Zysk = czynsz - wydatki
//        }
//        return Math.max(suma, 0);  // Zwracamy co najmniej 0
//    }
//
//    // Metoda generująca zbiorczą informację o lokalach w budynku
//    public String informacjaZbiorcza() {
//        StringBuilder info = new StringBuilder();
//        for (Lokal lokal : lokale) {
//            info.append(lokal.informacja()).append("\n");  // Łączy informacje o każdym lokalu
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
//        }
//        return info.toString();
//    }
//}
<<<<<<< HEAD
//

package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.ids.CassandraIds;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Getter;
import lombok.Setter;
=======


package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.google.gson.Gson;
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
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
=======
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
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
    }

    // Metoda do obliczenia całkowitego czynszu w budynku
    public double czynszCalkowity() {
        // This could be a query fetching all the Lokale and summing up their czynsz amounts.
        double suma = 0;
<<<<<<< HEAD
        for (UUID lokalId : lokale) {
            // Logic to fetch Lokale by UUID and sum up the czynsz, assuming a getCzynszByLokalId method
            suma += LokaleService.getCzynszByLokalId(lokalId);
=======
        for (Lokal lokal : getLokale()) {
            suma += lokal.czynsz();  // Zbiera czynsz ze wszystkich lokali
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
        }
        return suma;
    }

    // Metoda do obliczenia zysku z budynku, uwzględniając wydatki za metr
    public double zysk(double wydatkiZaMetr) {
        // Fetching Lokale and calculating the profit
        double suma = 0;
<<<<<<< HEAD
        for (UUID lokalId : lokale) {
            suma += LokaleService.getCzynszByLokalId(lokalId) - (LokaleService.getPowierzchniaByLokalId(lokalId) * wydatkiZaMetr);
=======
        for (Lokal lokal : getLokale()) {
            suma += lokal.czynsz() - (lokal.getPowierzchnia_w_metrach() * wydatkiZaMetr);  // Zysk = czynsz - wydatki
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
        }
        return Math.max(suma, 0);  // Zwracamy co najmniej 0
    }

    // Metoda generująca zbiorczą informację o lokalach w budynku
    public String informacjaZbiorcza() {
        // Gather information for each local using UUIDs
        StringBuilder info = new StringBuilder();
<<<<<<< HEAD
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
=======
        for (Lokal lokal : getLokale()) {
            info.append(lokal.informacja()).append("\n");  // Łączy informacje o każdym lokalu
        }
        return info.toString();
    }
>>>>>>> 2c9f1b9 (duzo zmian, jeszcze musze dorobic testy)
}

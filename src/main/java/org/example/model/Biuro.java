package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.UUID;

@Entity(defaultKeyspace = "buildings")  // Określenie odpowiedniego keyspace
@CqlName("biura")  // Określenie nazwy tabeli w bazie danych
public class Biuro extends Lokal {

    @CqlName("koszty_dodatkowe")  // Kolumna na dodatkowe koszty
    private double kosztyDodatkowe;

    public Biuro() {
        super(); // Konstruktor domyślny wymagany przez Cassandra
    }

    public Biuro(UUID id, double powierzchnia, double stawka, double kosztyDodatkowe) {
        super(id, powierzchnia, stawka, "Biuro");  // Przekazujemy typ "Biuro" do klasy nadrzędnej
        this.kosztyDodatkowe = kosztyDodatkowe;
    }

    @Override
    public double czynsz() {
        // Obliczanie czynszu uwzględnia powierzchnię, stawkę oraz dodatkowe koszty
        return (getPowierzchnia_w_metrach() * getStawka()) + dajKoszty();
    }

    public double dajKoszty() {
        return kosztyDodatkowe;
    }

    public void ustawKoszty(double koszty) {
        if (koszty > 0) {
            this.kosztyDodatkowe = koszty;
        }
    }

    @Override
    public String informacja() {
        // Nadpisanie metody informacja() z klasy nadrzędnej
        return "[Biuro] " + super.informacja() + " Koszty dodatkowe: " + kosztyDodatkowe;
    }

    // Gettery i settery dla Cassandra
    public double getKosztyDodatkowe() {
        return kosztyDodatkowe;
    }

    public void setKosztyDodatkowe(double kosztyDodatkowe) {
        this.kosztyDodatkowe = kosztyDodatkowe;
    }
}
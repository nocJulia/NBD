package org.example.mappers;

import com.datastax.oss.driver.api.core.cql.Row;
import org.example.model.Biuro;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;

import java.util.UUID;

public class FromDBLokalMapper {

    // Metoda mapująca wiersz Cassandra do obiektu Mieszkanie
    public static Mieszkanie getMieszkanie(Row row) {
        // Mieszkanie ma powierzchnię i stawkę, a także dziedziczy po Lokalu
        return new Mieszkanie(
                UUID.fromString(row.getString("id")),
                row.getDouble("powierzchnia"),  // powierzchnia w metrach
                row.getDouble("stawka"),  // stawka za metr kwadratowy
                row.getInt("liczba_pokoi")
        );
    }

    // Metoda mapująca wiersz Cassandra do obiektu Biuro
    public static Biuro getBiuro(Row row) {
        // Biuro ma powierzchnię, stawkę i koszty dodatkowe
        return new Biuro(
                UUID.fromString(row.getString("id")),  // UUID dla budynku
                row.getDouble("powierzchnia"),  // powierzchnia w metrach
                row.getDouble("stawka"),  // stawka za metr kwadratowy
                row.getDouble("koszty_dodatkowe")  // dodatkowe koszty związane z biurem
        );
    }
}

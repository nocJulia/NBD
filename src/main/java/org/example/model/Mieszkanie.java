package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import java.util.UUID;

@Entity(defaultKeyspace = "buildings")  // Użyj odpowiedniego keyspace
@CqlName("mieszkania")  // Nazwa tabeli w bazie danych
public class Mieszkanie extends Lokal {

    @CqlName("liczba_pokoi")
    private int liczbaPokoi;  // Dodajemy pole liczba pokoi

    public Mieszkanie(UUID id, double powierzchnia, double stawka, int liczbaPokoi) {
        super(id, powierzchnia, stawka, "Mieszkanie");  // Typ ustawiony na "Mieszkanie"
        this.liczbaPokoi = liczbaPokoi;
    }

    public Mieszkanie() {
        // Konstruktor domyślny
    }

    public int getLiczbaPokoi() {
        return liczbaPokoi;
    }

    public void setLiczbaPokoi(int liczbaPokoi) {
        this.liczbaPokoi = liczbaPokoi;
    }

    @Override
    public double czynsz() {
        // Obliczanie czynszu zależne od powierzchni i stawki, możesz dodać logikę z liczbaPokoi
        return getPowierzchnia_w_metrach() * getStawka();  // Przykład z mnożnikiem
    }

    @Override
    public String informacja() {
        // Dodanie specyficznych informacji o mieszkaniu
        return "[Mieszkanie] " + super.informacja() + " | Liczba pokoi: " + liczbaPokoi;
    }
}
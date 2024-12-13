package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import java.util.UUID;

@Entity(defaultKeyspace = "buildings")
@CqlName("mieszkania")
public class Mieszkanie extends Lokal {

    @CqlName("liczba_pokoi")
    private int liczbaPokoi;

    public Mieszkanie(UUID id, double powierzchnia_w_metrach, double stawka, int liczbaPokoi) {
        super(id, powierzchnia_w_metrach, stawka, "Mieszkanie");
        this.liczbaPokoi = liczbaPokoi;
    }

    public Mieszkanie() {
    }

    public int getLiczbaPokoi() {
        return liczbaPokoi;
    }

    public void setLiczbaPokoi(int liczbaPokoi) {
        this.liczbaPokoi = liczbaPokoi;
    }

    @Override
    public double czynsz() {
        return getPowierzchnia_w_metrach() * getStawka();  // Przykład z mnożnikiem
    }

    @Override
    public String informacja() {
        return "[Mieszkanie] " + super.informacja() + " | Liczba pokoi: " + liczbaPokoi;
    }
}

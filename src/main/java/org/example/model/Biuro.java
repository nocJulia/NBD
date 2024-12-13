package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.UUID;

@Entity(defaultKeyspace = "buildings")
@CqlName("biura")
public class Biuro extends Lokal {

    @CqlName("koszty_dodatkowe")
    private double kosztyDodatkowe;

    public Biuro() {
        super();
    }

    public Biuro(UUID id, double powierzchnia_w_metrach, double stawka, double kosztyDodatkowe) {
        super(id, powierzchnia_w_metrach, stawka, "Biuro");
        this.kosztyDodatkowe = kosztyDodatkowe;
    }

    @Override
    public double czynsz() {
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
        return "[Biuro] " + super.informacja() + " Koszty dodatkowe: " + kosztyDodatkowe;
    }

    public double getKosztyDodatkowe() {
        return kosztyDodatkowe;
    }

    public void setKosztyDodatkowe(double kosztyDodatkowe) {
        this.kosztyDodatkowe = kosztyDodatkowe;
    }
}
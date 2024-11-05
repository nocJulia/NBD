package org.example.model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;

public class Biuro extends Lokal {
    @BsonProperty("kosztyDodatkowe")
    private double kosztyDodatkowe;

    // BsonCreator constructor for MongoDB document mapping
    @BsonCreator
    public Biuro(UniqueIdMgd entityId,@BsonProperty("powierzchnia") double powierzchnia,
                 @BsonProperty("stawka") double stawka,
                 @BsonProperty("kosztyDodatkowe") double kosztyDodatkowe) {
        super(entityId, powierzchnia, stawka);
        this.kosztyDodatkowe = kosztyDodatkowe;
    }

    @Override
    public double czynsz() {
        return (dajPowierzchnie() * dajStawke()) + dajKoszty();
    }

    @BsonProperty("kosztyDodatkowe")
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
}

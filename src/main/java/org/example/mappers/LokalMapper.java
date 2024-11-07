package org.example.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Biuro;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.repository.BudynekRepository;

public class LokalMapper implements Mapper<Lokal> {

    private BudynekRepository budynekRepository;

    @Override
    public Document toDocument(Lokal entity) {
        Document document = new Document();

        document.put("_id", entity.get_id().toHexString());
        document.put("powierzchnia", entity.dajPowierzchnie());
        document.put("stawka", entity.dajStawke());

        // Serializacja ID budynku
        if (entity.getBudynek() != null) {
            document.put("budynek", entity.getBudynek().get_id().toHexString());
        }

        // Zapisz typ i specyficzne dane dla podklas
        if (entity instanceof Biuro biuro) {
            document.put("type", "Biuro");
            document.put("kosztyDodatkowe", biuro.dajKoszty());
        } else if (entity instanceof Mieszkanie) {
            document.put("type", "Mieszkanie");
            // Brak dodatkowych atrybutów dla Mieszkanie
        } else {
            throw new IllegalArgumentException("Unsupported lokal type: " + entity.getClass().getSimpleName());
        }

        return document;
    }

    @Override
    public Lokal fromDocument(Document document) {
        ObjectId _id = new ObjectId(document.getString("_id"));
        double powierzchnia = document.getDouble("powierzchnia");
        double stawka = document.getDouble("stawka");

        String budynekId = document.getString("budynek");
        Budynek budynek = findById(budynekId);  // Funkcja do pobrania obiektu Budynek na podstawie ID

        String type = document.getString("type");

        switch (type) {
            case "Biuro" -> {
                double kosztyDodatkowe = document.getDouble("kosztyDodatkowe");
                return new Biuro(_id, budynek, powierzchnia, stawka, kosztyDodatkowe);
            }
            case "Mieszkanie" -> {
                return new Mieszkanie(_id, budynek, powierzchnia, stawka);
            }
            default -> throw new IllegalArgumentException("Unknown lokal type: " + type);
        }
    }

    private Budynek findById(String budynekId) {
        if (budynekId == null) {
            return null;
        }

        ObjectId id = new ObjectId(budynekId);
        return budynekRepository.findById(String.valueOf(id));
    }
}


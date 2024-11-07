package org.example.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Budynek;
import org.example.model.Lokal;

import java.util.List;
import java.util.stream.Collectors;

public class BudynekMapper {

    private final LokalMapper lokalMapper; // Potrzebny mapper do obsługi klasy Lokal

    public BudynekMapper(LokalMapper lokalMapper) {
        this.lokalMapper = lokalMapper;
    }

    // Metoda konwertująca Budynek na Document
    public Document toDocument(Budynek budynek) {
        Document document = new Document();

        document.put("_id", budynek.get_id().toHexString());  // Konwersja ObjectId na String
        document.put("nazwa", budynek.getNazwa());

        // Konwersja listy obiektów Lokal do listy dokumentów BSON
        List<Document> lokaleDocuments = budynek.getLokale().stream()
                .map(lokalMapper::toDocument)  // Użycie lokalMapper do konwersji każdego Lokal
                .collect(Collectors.toList());

        document.put("lokale", lokaleDocuments);

        return document;
    }

    // Metoda konwertująca Document na Budynek
    public Budynek fromDocument(Document document) {
        ObjectId _id = new ObjectId(document.getString("_id"));
        String nazwa = document.getString("nazwa");

        // Pobranie listy dokumentów "lokale" i konwersja na listę obiektów Lokal
        List<Document> lokaleDocs = document.getList("lokale", Document.class);
        List<Lokal> lokale = lokaleDocs.stream()
                .map(lokalMapper::fromDocument)  // Konwersja każdego dokumentu na obiekt Lokal
                .collect(Collectors.toList());

        Budynek budynek = new Budynek(nazwa, lokale);
//        budynek.set_id(_id);  // Ustawienie identyfikatora

        return budynek;
    }
}

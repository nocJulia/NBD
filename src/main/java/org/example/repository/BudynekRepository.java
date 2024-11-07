//package org.example.repository;
//
//import org.example.model.Budynek;
//
//import java.util.List;
//
//public interface BudynekRepository {
//    void save(Budynek budynek);
//    Budynek findById(Long id);
//    List<Budynek> findAll();
//    void delete(Budynek budynek);
//}

package org.example.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.mappers.BudynekMapper;

import java.util.ArrayList;
import java.util.List;

public class BudynekRepository extends AbstractMongoRepository implements Repository<Budynek> {

    private final MongoCollection<Document> collection;
    private final BudynekMapper budynekMapper;

    public BudynekRepository() {
        super.initDbConnection();
        this.collection = mongoDatabase.getCollection("budynki"); // Kolekcja "budynki"
        this.budynekMapper = new BudynekMapper(new LokalMapper()); // Inicjalizacja mappera Budynek
    }

    @Override
    public void save(Budynek budynek) {
        Document document = budynekMapper.toDocument(budynek);
        collection.insertOne(document);
    }

    @Override
    public Budynek findById(String id) {
        Document doc = collection.find(new Document("_id", id)).first();
        if (doc != null) {
            return budynekMapper.fromDocument(doc);
        }
        return null;
    }

    @Override
    public List<Budynek> findAll() {
        List<Budynek> budynki = new ArrayList<>();
        for (Document doc : collection.find()) {
            budynki.add(budynekMapper.fromDocument(doc));
        }
        return budynki;
    }

    @Override
    public void update(Budynek budynek) {
        Document query = new Document("_id", budynek.get_id());
        Document updatedDoc = budynekMapper.toDocument(budynek);
        collection.replaceOne(query, updatedDoc);
    }

    @Override
    public void delete(Budynek budynek) {
        Document query = new Document("_id", budynek.get_id());
        collection.deleteOne(query);
    }

    @Override
    public int size() {
        return (int) collection.countDocuments();
    }

    public void deleteAll() {
        collection.deleteMany(new Document()); // Usuwa wszystkie dokumenty w kolekcji
    }

}


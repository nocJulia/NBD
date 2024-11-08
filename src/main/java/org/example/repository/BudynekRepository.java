package org.example.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.mappers.BudynekMapper;

import java.util.ArrayList;
import java.util.List;

public class BudynekRepository extends AbstractMongoRepository implements Repository<Budynek> {

    private final MongoCollection<Document> collection;
    private final BudynekMapper budynekMapper;

//    public BudynekRepository() {
//        super.initDbConnection();
//        this.collection = mongoDatabase.getCollection("budynki");
//
//        // Przekazanie BudynekRepository do LokalMapper
//        this.budynekMapper = new BudynekMapper(this);
//    }

    public BudynekRepository(LokalMapper lokalMapper) {
        super.initDbConnection();
        this.collection = mongoDatabase.getCollection("budynki"); // Kolekcja "budynki"
        this.budynekMapper = new BudynekMapper(lokalMapper);
    }

    public void setLokalMapper(LokalMapper lokalMapper) {
        this.budynekMapper.setLokalMapper(lokalMapper);
    }

    @Override
    public void save(Budynek budynek) {
        Document document = budynekMapper.toDocument(budynek);
        collection.insertOne(document);
    }

    @Override
    public Budynek findById(ObjectId id) {
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
        Document query = new Document("_id", budynek.getId());
        Document updatedDoc = budynekMapper.toDocument(budynek);
        collection.replaceOne(query, updatedDoc);
    }

    @Override
    public void delete(Budynek budynek) {
        Document query = new Document("_id", budynek.getId());
        collection.deleteOne(query);
    }

    @Override
    public int size() {
        return (int) collection.countDocuments();
    }

    public void clearCollection() {
        collection.deleteMany(new Document()); // Usuwa wszystkie dokumenty z kolekcji "budynki"
    }

}


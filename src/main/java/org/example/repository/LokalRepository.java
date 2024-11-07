//package org.example.repository;
//
//import org.example.model.Lokal;
//
//import java.util.List;
//
//public interface LokalRepository {
//    void save(Lokal lokal);
//    Lokal findById(Long id);
//    List<Lokal> findAll();
//    void delete(Lokal lokal);
//}

package org.example.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.model.Lokal;
import org.example.mappers.LokalMapper;

import java.util.ArrayList;
import java.util.List;

public class LokalRepository extends AbstractMongoRepository implements Repository<Lokal> {

    private final MongoCollection<Document> collection;
    private final LokalMapper lokalMapper;

    public LokalRepository() {
        super.initDbConnection();
        this.collection = mongoDatabase.getCollection("lokale"); // Kolekcja "lokale"
        this.lokalMapper = new LokalMapper(); // Inicjalizacja mappera Lokal
    }

    @Override
    public void save(Lokal lokal) {
        Document document = lokalMapper.toDocument(lokal);
        collection.insertOne(document);
    }

    @Override
    public Lokal findById(String id) {
        Document doc = collection.find(new Document("_id", id)).first();
        if (doc != null) {
            return lokalMapper.fromDocument(doc);
        }
        return null;
    }

    @Override
    public List<Lokal> findAll() {
        List<Lokal> lokale = new ArrayList<>();
        for (Document doc : collection.find()) {
            lokale.add(lokalMapper.fromDocument(doc));
        }
        return lokale;
    }

    @Override
    public void update(Lokal lokal) {
        Document query = new Document("_id", lokal.get_id());
        Document updatedDoc = lokalMapper.toDocument(lokal);
        collection.replaceOne(query, updatedDoc);
    }

    @Override
    public void delete(Lokal lokal) {
        Document query = new Document("_id", lokal.get_id());
        collection.deleteOne(query);
    }

    @Override
    public int size() {
        return (int) collection.countDocuments();
    }
}


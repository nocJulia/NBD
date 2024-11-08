package org.example.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Lokal;
import org.example.mappers.LokalMapper;

import java.util.ArrayList;
import java.util.List;

public class LokalRepository extends AbstractMongoRepository implements Repository<Lokal> {

    private final MongoCollection<Document> collection;
    private final LokalMapper lokalMapper;

    // Konstruktor przyjmujący LokalMapper jako zależność
    public LokalRepository(LokalMapper lokalMapper) {
        super.initDbConnection();
        this.collection = mongoDatabase.getCollection("lokale"); // Kolekcja "lokale"
        this.lokalMapper = lokalMapper; // Używamy przekazanej instancji LokalMapper
    }

    @Override
    public void save(Lokal lokal) {
        Document document = lokalMapper.toDocument(lokal);
        collection.insertOne(document);
    }

    @Override
    public Lokal findById(ObjectId id) {
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
        Document query = new Document("_id", lokal.getId());
        Document updatedDoc = lokalMapper.toDocument(lokal);
        collection.replaceOne(query, updatedDoc);
    }

    @Override
    public void delete(Lokal lokal) {
        Document query = new Document("_id", lokal.getId());
        collection.deleteOne(query);
    }

    @Override
    public int size() {
        return (int) collection.countDocuments();
    }

    public void clearCollection() {
        collection.deleteMany(new Document()); // Usuwa wszystkie dokumenty z kolekcji "lokale"
    }
}

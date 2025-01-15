package org.example;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.exceptions.MongoDBException;

public class MessageSaver {
    private final MongoCollection<Document> mongoCollection;

    public MessageSaver() {

        try  {
            MongoRepo repo = new MongoRepo();
            this.mongoCollection = repo.getDatabase().getCollection("buildings_message_db");
        } catch (MongoException e) {
            throw new MongoDBException("Error while creating database"+ e.getMessage());
        }
    }


    public synchronized void saveToMongo(String message) {
        try {
            Document jsonDocument = new Document().append("reservations", message);
            mongoCollection.insertOne(jsonDocument);
            System.out.println("Saved to database: " + message);
        } catch (MongoException e) {
            System.err.println("Error saving to MongoDB: " + e.getMessage());
        }
    }
}

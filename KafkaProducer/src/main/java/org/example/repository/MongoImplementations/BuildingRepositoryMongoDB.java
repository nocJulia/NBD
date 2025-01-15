package org.example.repository.MongoImplementations;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.example.model.Building;
import org.example.repository.BuildingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingRepositoryMongoDB implements BuildingRepository {

    private final MongoCollection<Building> mongoCollection;
    private final MongoClient mongoClient;  // MongoClient to create the session

    // Konstruktor przyjmujący MongoClient i MongoCollection
    public BuildingRepositoryMongoDB(MongoCollection<Building> mongoCollection, MongoClient mongoClient) {
        this.mongoCollection = mongoCollection;
        this.mongoClient = mongoClient;
    }

    @Override
    public Building saveBuilding(Building building) {
        mongoCollection.updateOne(
                Filters.eq("_id", building.getId()),
                new Document("$set", building), // Aktualizuj wszystkie pola
                new UpdateOptions().upsert(true) // Jeśli brak, wstaw nowy
        );
        return building;
    }


    @Override
    public Building updateHistoricStatus(UUID id, boolean isHistoric) {
        return mongoCollection.findOneAndUpdate(Filters.eq("_id", id),
                Updates.set("is_historic", isHistoric),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }

    @Override
    public Building updateNumberOfFloors(UUID id, int numberOfFloors) {
        return mongoCollection.findOneAndUpdate(Filters.eq("_id", id),
                Updates.set("number_of_floors", numberOfFloors),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }

    @Override
    public Building findBuildingById(UUID id) {
        return mongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Building> findAllBuildings() {
        return mongoCollection.find().into(new ArrayList<>());
    }

    @Override
    public boolean deleteBuilding(UUID id) {
        Building deletedBuilding = mongoCollection.findOneAndDelete(Filters.eq("_id", id));
        return deletedBuilding != null;
    }

    // Tworzymy sesję za pomocą MongoClient
    public ClientSession getClientSession() {
        return mongoClient.startSession();  // Uzyskujemy sesję z MongoClient
    }
}


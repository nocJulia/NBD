package org.example.managers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.example.model.Building;
import org.example.repository.MongoImplementations.BuildingRepositoryMongoDB;
import org.example.repository.BuildingRepository;

import java.util.List;
import java.util.UUID;

public class BuildingManager {
    private final BuildingRepository buildingRepository;

    public BuildingManager(MongoCollection<Building> collection, MongoClient client) {
        this.buildingRepository = new BuildingRepositoryMongoDB(collection, client);
    }

    public Building getBuilding(UUID id) {
        return buildingRepository.findBuildingById(id);
    }

    public Building registerBuilding(String buildingName, double constructionCost, double maintenanceCost, int numberOfFloors, String description) {
        Building building = new Building(buildingName, constructionCost, maintenanceCost, numberOfFloors, description);
        return buildingRepository.saveBuilding(building);
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAllBuildings();
    }
}

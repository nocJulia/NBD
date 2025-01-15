package org.example.repository;

import com.mongodb.client.ClientSession;
import org.example.model.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository {
    Building saveBuilding(Building building);
    Building updateHistoricStatus(UUID id, boolean isHistoric);
    Building updateNumberOfFloors(UUID id, int numberOfFloors);
    Building findBuildingById(UUID id);
    List<Building> findAllBuildings();
    boolean deleteBuilding(UUID id);

    ClientSession getClientSession();
}

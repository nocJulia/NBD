package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class BuildingEntry {

    private Building building;
    private int quantity;

    public BuildingEntry(Building building, int quantity) {
        this.building = building;
        this.quantity = quantity;
    }

    public double getTotalConstructionCost() {
        return building.getConstructionCost() * quantity;
    }

    public double getTotalMaintenanceCost() {
        return building.getMaintenanceCost() * quantity;
    }
}

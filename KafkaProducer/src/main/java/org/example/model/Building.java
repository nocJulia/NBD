package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Building extends AbstractEntity {

    @BsonProperty("building_name")
    private String buildingName;
    @BsonProperty("construction_cost")
    private double constructionCost;
    @BsonProperty("maintenance_cost")
    private double maintenanceCost;
    @BsonProperty("is_historic")
    private boolean isHistoric;
    @BsonProperty("number_of_floors")
    private int numberOfFloors;
    @BsonProperty("description")
    private String description;

    @BsonProperty("is_sold")
    private boolean isSold; // Pole dla stanu sprzedaży

    @BsonProperty("is_rented")
    private boolean isRented; // Pole dla stanu wynajmu

    @BsonCreator
    public Building(@BsonProperty("_id") UUID id,
                    @BsonProperty("building_name") String buildingName,
                    @BsonProperty("construction_cost") double constructionCost,
                    @BsonProperty("maintenance_cost") double maintenanceCost,
                    @BsonProperty("is_historic") boolean isHistoric,
                    @BsonProperty("number_of_floors") int numberOfFloors,
                    @BsonProperty("description") String description,
                    @BsonProperty("is_sold") boolean isSold,
                    @BsonProperty("is_rented") boolean isRented) {
        super(id);
        this.buildingName = buildingName;
        this.constructionCost = constructionCost;
        this.maintenanceCost = maintenanceCost;
        this.isHistoric = isHistoric;
        this.numberOfFloors = numberOfFloors;
        this.description = description;
        this.isSold = isSold;
        this.isRented = isRented;
    }

    public Building(String buildingName,
                    double constructionCost,
                    double maintenanceCost,
                    int numberOfFloors,
                    String description) {
        super(UUID.randomUUID());
        this.buildingName = buildingName;
        this.constructionCost = constructionCost;
        this.maintenanceCost = maintenanceCost;
        this.numberOfFloors = numberOfFloors;
        this.description = description;
        this.isSold = false; // Domyślnie budynek nie jest sprzedany
        this.isRented = false; // Domyślnie budynek nie jest wynajęty
    }
}

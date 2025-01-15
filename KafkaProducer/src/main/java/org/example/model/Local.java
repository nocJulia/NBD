package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.model.type.LocalType;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Local extends AbstractEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("address")
    private Address address;

    @BsonProperty("local_type")
    private LocalType localType;

    @BsonProperty("visitors_count")
    private int visitorsCount = 0;

    public Local(String name, Address address, LocalType localType) {
        super(UUID.randomUUID());
        this.name = name;
        this.address = address;
        this.localType = localType;
    }

    @BsonIgnore
    public double getLocalAttractivenessScore() {
        return localType.getAttractivenessScore();
    }

    @BsonIgnore
    public int getLocalPriorityLevel() {
        return localType.getPriorityLevel();
    }

    public void incrementVisitorsCount() {
        visitorsCount++;
    }

    @BsonCreator
    public Local(@BsonProperty("_id") UUID entityId,
                 @BsonProperty("name") String name,
                 @BsonProperty("local_type") LocalType localType,
                 @BsonProperty("visitors_count") int visitorsCount,
                 @BsonProperty("address") Address address) {
        super(entityId);
        this.name = name;
        this.localType = localType;
        this.visitorsCount = visitorsCount;
        this.address = address;
    }
}

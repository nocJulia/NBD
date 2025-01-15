package org.example.model.type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
public abstract class LocalType {
    @BsonProperty("attractiveness_score")
    protected double attractivenessScore;

    @BsonProperty("priority_level")
    protected int priorityLevel;

    @BsonCreator
    public LocalType(@BsonProperty("attractiveness_score") double attractivenessScore,
                     @BsonProperty("priority_level") int priorityLevel) {
        this.attractivenessScore = attractivenessScore;
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String toString() {
        return "LocalType{" +
                "attractivenessScore=" + attractivenessScore +
                ", priorityLevel=" + priorityLevel +
                '}';
    }
}

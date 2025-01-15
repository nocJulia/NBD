package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class AbstractEntity implements Serializable {

    @BsonProperty("_id")
    private UUID entityId;
    public AbstractEntity(UUID entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(entityId, that.entityId);
    }
    public UUID getId(){
        return entityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }
}

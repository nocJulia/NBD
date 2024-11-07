//package org.example.model;
//
//import org.bson.codecs.pojo.annotations.BsonProperty;
//import java.io.Serializable;
//
//// Abstrakcyjna klasa, z której będą dziedziczyć inne modele
//public abstract class AbstractEntityMgd implements Serializable {
//
//    @BsonProperty("_id")
//    private final UniqueIdMgd entityId;
//
//    // Konstruktor przyjmujący entityId
//    public AbstractEntityMgd(UniqueIdMgd entityId) {
//        this.entityId = entityId;
//    }
//
//    // Getter do pobierania entityId
//    public UniqueIdMgd getEntityId() {
//        return entityId;
//    }
//}

package org.example.validation;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MongoSchemaValidator {

    public static void createBudynekValidationSchema(MongoDatabase database) {
        Document budynekValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("_id", "nazwa", "lokale"))
                        .append("properties", new Document()
                                .append("_id", new Document("bsonType", "objectId"))
                                .append("nazwa", new Document("bsonType", "string")
                                        .append("minLength", 1)
                                        .append("maxLength", 100))
                                .append("lokale", new Document("bsonType", "array")
                                        .append("items", new Document("bsonType", "object")))));

        database.createCollection("budynki");
        database.runCommand(new Document("collMod", "budynki")
                .append("validator", budynekValidator)
                .append("validationLevel", "strict")
                .append("validationAction", "error"));
    }

    public static void createLokalValidationSchema(MongoDatabase database) {
        Document lokalValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("_id", "type", "powierzchnia", "stawka", "budynek"))
                        .append("properties", new Document()
                                .append("_id", new Document("bsonType", "objectId"))
                                .append("type", new Document("enum", Arrays.asList("Biuro", "Mieszkanie")))
                                .append("powierzchnia", new Document("bsonType", "double")
                                        .append("minimum", 0))
                                .append("stawka", new Document("bsonType", "double")
                                        .append("minimum", 0))
                                .append("budynek", new Document("bsonType", "objectId"))
                                .append("kosztyDodatkowe", new Document("bsonType", "double")
                                        .append("minimum", 0))));

        database.createCollection("lokale");
        database.runCommand(new Document("collMod", "lokale")
                .append("validator", lokalValidator)
                .append("validationLevel", "strict")
                .append("validationAction", "error"));
    }
}

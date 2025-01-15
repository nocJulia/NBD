package org.example.model;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import lombok.Getter;
import org.bson.Document;
import org.example.managers.BuildingManager;
import org.example.managers.ClientManager;
import org.example.managers.LocalManager;
import org.example.managers.TransactionManager;
import org.example.repository.AbstractMongoRepository;

import java.util.concurrent.ExecutionException;

@Getter
public class Shop implements AutoCloseable {
    private final ClientManager clientManager;
    private final BuildingManager buildingManager;
    private final TransactionManager transactionManager;
    private final AbstractMongoRepository repository;
    private final LocalManager localManager;

    public Shop() throws ExecutionException, InterruptedException {
        repository = new AbstractMongoRepository();
        createBuildingCollectionWithValidations();
        createTransactionCollectionWithValidations();
        createClientCollectionWithValidations();
        createLocalCollectionWithValidations();

        clientManager = new ClientManager(repository.getDatabase().getCollection("clients", Client.class));

        MongoCollection<Building> buildingCollection = repository.getDatabase().getCollection("buildings", Building.class);
        MongoCollection<Client> clientCollection = repository.getDatabase().getCollection("clients", Client.class);
        MongoCollection<Transaction> transactionCollection = repository.getDatabase().getCollection("transactions", Transaction.class);
        MongoCollection<Local> localCollection = repository.getDatabase().getCollection("local", Local.class);

        buildingManager = new BuildingManager(buildingCollection, repository.getMongoClient());
        transactionManager = new TransactionManager(transactionCollection, buildingCollection, clientCollection, repository.getMongoClient());
        localManager = new LocalManager(localCollection);
    }

    private void createLocalCollectionWithValidations() {
        // Definiowanie walidatora w formacie JSON Schema
        Document validator = Document.parse("""
                        {
                            $jsonSchema: {
                                "bsonType": "object",
                                "required": ["name", "address", "local_type", "visitors_count"],
                                "properties": {
                                    "name": {
                                        "bsonType": "string"
                                    },
                                    "address": {
                                        "bsonType": "object",
                                        "properties": {
                                            "street": {
                                                "bsonType": "string"
                                            },
                                            "city": {
                                                "bsonType": "string"
                                            },
                                            "postal_code": {
                                                "bsonType": "string"
                                            }
                                        },
                                        "required": ["street", "city", "postal_code"]
                                    },
                                    "local_type": {
                                        "bsonType": "string"
                                    },
                                    "visitors_count": {
                                        "bsonType": "int",
                                        "minimum": 0
                                    }
                                }
                            }
                        }
                    """);

        try {
            // Tworzenie opcji walidacji z wykorzystaniem validatora
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);

            // Tworzenie kolekcji "locals" z odpowiednią walidacją
            repository.getDatabase().createCollection("locals", createCollectionOptions);
        } catch (MongoCommandException ignored) {
            // Jeśli kolekcja już istnieje, wykonujemy modyfikację istniejącej kolekcji
            Document command = new Document("collMod", "locals").append("validator", validator);
            repository.getDatabase().runCommand(command);
        }
    }


    // Tworzymy kolekcję dla transakcji z odpowiednimi walidacjami
    private void createTransactionCollectionWithValidations() {
        Document validator = Document.parse("""
                            {
                                $jsonSchema:{
                                    "bsonType": "object",
                                    "required": ["client", "building", "transaction_type", "cost",],
                                    "properties": {
                                        "client": {
                                            "bsonType": "object"
                                        },
                                        "building": {
                                            "bsonType": "object"
                                        },
                                        "transaction_type": {
                                            "bsonType": "string"
                                        },
                                        "cost": {
                                            "bsonType": "double",
                                            "minimum": 0
                                        }
                                    }
                                }
                            }
                        """);

        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            repository.getDatabase().createCollection("transactions", createCollectionOptions);
        } catch (MongoCommandException ignored) {
            Document command = new Document("collMod", "transactions")
                    .append("validator", validator);
            repository.getDatabase().runCommand(command);
        }
    }

    // Tworzymy kolekcję dla budynków z odpowiednimi walidacjami
    private void createBuildingCollectionWithValidations() {
        Document validator = Document.parse("""
                            {
                                $jsonSchema:{
                                    "bsonType": "object",
                                    "required": ["building_name", "construction_cost", "maintenance_cost", "number_of_floors", "description"],
                                    "properties": {
                                        "building_name": {
                                            "bsonType": "string"
                                        },
                                        "construction_cost": {
                                            "bsonType": "double"
                                        },
                                        "maintenance_cost": {
                                            "bsonType": "double"
                                        },
                                        "number_of_floors": {
                                            "bsonType": "int",
                                            "minimum": 0
                                        },
                                        "description": {
                                            "bsonType": "string"
                                        }
                                    }
                                }
                            }
                        """);

        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            repository.getDatabase().createCollection("buildings", createCollectionOptions);
        } catch (MongoCommandException ignored) {
            Document command = new Document("collMod", "buildings")
                    .append("validator", validator);
            repository.getDatabase().runCommand(command);
        }
    }

    // Tworzymy kolekcję dla klientów z odpowiednimi walidacjami
    private void createClientCollectionWithValidations() {
        Document validator = Document.parse("""
                            {
                                $jsonSchema:{
                                    "bsonType": "object",
                                    "required": ["name", "email", "phone_number"],
                                    "properties": {
                                        "name": {
                                            "bsonType": "string"
                                        },
                                        "email": {
                                            "bsonType": "string"
                                        },
                                        "phone_number": {
                                            "bsonType": "string"
                                        }
                                    }
                                }
                            }
                        """);

        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            repository.getDatabase().createCollection("clients", createCollectionOptions);
        } catch (MongoCommandException ignored) {
            Document command = new Document("collMod", "clients")
                    .append("validator", validator);
            repository.getDatabase().runCommand(command);
        }
    }

    // Zamykanie zasobów
    @Override
    public void close() {
        repository.close();
    }
}


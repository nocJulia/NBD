package org.example.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
//import org.example.CustomCodecProvider;

import java.util.List;

@Getter
public class AbstractMongoRepository implements AutoCloseable {
    private final ConnectionString connectionString = new ConnectionString(
            "mongodb://localhost:27017,localhost:27018,localhost:27019/buildings?replicaSet=replica_set_single"
    );
    private final MongoCredential credential = MongoCredential.createCredential("admin",
            "admin", "adminpassword".toCharArray());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());

    private MongoClient mongoClient;
    private MongoDatabase database;

    public ClientSession getClientSession(){
        return mongoClient.startSession();
    }


    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("buildings");
    }

    public AbstractMongoRepository() {
        initDbConnection();
    }

    @Override
    public void close() {
        //database.drop();
        //mongoClient.close();
    }
}
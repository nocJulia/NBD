package org.example;

import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.example.model.Client;
import org.example.repository.AbstractMongoRepository;
import org.example.repository.ClientRepository;
import org.example.repository.MongoImplementations.ClientRepositoryMongoDB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest {

    static AbstractMongoRepository repository = new AbstractMongoRepository();
    static MongoDatabase mongoDatabase = repository.getDatabase();
    static ClientRepository clientRepository = new ClientRepositoryMongoDB(mongoDatabase.getCollection("clients_test", Client.class));

    // Initialize test clients

    Client client = new Client("Julia", "Nocun", "334333444");
    Client client2 = new Client("Tycjan", "Lamkiewicz", "999222111");


    @Test
    void saveClientTest() {
        assertEquals(clientRepository.saveClient(client), client);
    }

    @Test
    void deleteClientTest() {
        clientRepository.saveClient(client2);
        assertTrue(clientRepository.deleteClient(client2.getEntityId()));
    }

    @Test
    void findClientByIdTest() {
        Client client = clientRepository.saveClient(client2);
        assertEquals(clientRepository.findClientById(client2.getEntityId()), client);
    }

    @Test
    void findAllClients(){
        assertEquals(clientRepository.findAllClients().size(), 2);
    }

    @AfterAll
    static void cleanDataBase() {
        assertEquals(clientRepository.findAllClients().size(), 2);
        mongoDatabase.getCollection("clients_test").drop();
    }
}
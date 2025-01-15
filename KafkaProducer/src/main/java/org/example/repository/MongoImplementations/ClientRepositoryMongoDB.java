package org.example.repository.MongoImplementations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.example.model.Client;
import org.example.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepositoryMongoDB implements ClientRepository {

    private final MongoCollection<Client> clientCollection;

    public ClientRepositoryMongoDB(MongoCollection<Client> clientCollection) {
        this.clientCollection = clientCollection;
    }

    @Override
    public Client findClientById(UUID id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Client> findAllClients() {
        return clientCollection.find().into(new ArrayList<>());
    }

    @Override
    public Client saveClient(Client client) {
        clientCollection.insertOne(client);
        return client;
    }

    @Override
    public boolean deleteClient(UUID id) {
        return clientCollection.deleteOne(Filters.eq("_id", id)).getDeletedCount() > 0;
    }
}

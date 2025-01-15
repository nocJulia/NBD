package org.example.managers;

import com.mongodb.client.MongoCollection;
import org.example.model.Client;
import org.example.repository.ClientRepository;
import org.example.repository.MongoImplementations.ClientRepositoryMongoDB;

import java.util.List;
import java.util.UUID;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(MongoCollection<Client> clientCollection) {
        this.clientRepository = new ClientRepositoryMongoDB(clientCollection);
    }

    public Client registerClient(String name, String email, String phoneNumber) {
        Client client = new Client(name, email, phoneNumber);
        return clientRepository.saveClient(client);
    }

    public Client getClient(UUID id) {
        return clientRepository.findClientById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAllClients();
    }

    public boolean deleteClient(UUID id) {
        return clientRepository.deleteClient(id);
    }
}

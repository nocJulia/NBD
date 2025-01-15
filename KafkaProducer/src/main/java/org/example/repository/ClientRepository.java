package org.example.repository;

import org.example.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    Client findClientById(UUID id);
    List<Client> findAllClients();
    Client saveClient(Client client);
    boolean deleteClient(UUID id);
}

package org.example.managers;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.example.exceptions.InvalidTransactionException;
import org.example.model.*;
import org.example.repository.MongoImplementations.BuildingRepositoryMongoDB;
import org.example.repository.MongoImplementations.TransactionRepositoryMongoDB;
import org.example.repository.BuildingRepository;
import org.example.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

public class TransactionManager {
    private final TransactionRepository transactionRepository;
    private final BuildingRepository buildingRepository;

    private final ClientManager clientManager;

    public TransactionManager(MongoCollection<Transaction> transactionCollection,
                              MongoCollection<Building> buildingCollection,
                              MongoCollection<Client> clientCollection,
                              MongoClient mongoClient) {
        this.transactionRepository = new TransactionRepositoryMongoDB(transactionCollection);
        this.buildingRepository = new BuildingRepositoryMongoDB(buildingCollection, mongoClient);
        this.clientManager = new ClientManager(clientCollection);
    }


    public Transaction getTransaction(UUID id) {
        return transactionRepository.findTransactionById(id);
    }

    public Transaction processTransaction(Client client, Building building, TransactionType type, double cost) {
        if (building.isSold() || (type == TransactionType.RENT && building.isRented())) {
            throw new InvalidTransactionException("The building is not available for the requested transaction.");
        }

        Transaction transaction = new Transaction(client, building, type, cost);

        try (ClientSession session = buildingRepository.getClientSession()) {
            session.startTransaction();
            if (type == TransactionType.SALE) {
                building.setSold(true);
            } else if (type == TransactionType.RENT) {
                building.setRented(true);
            }
            buildingRepository.saveBuilding(building);
            transactionRepository.saveTransaction(transaction);
            session.commitTransaction();
        } catch (Exception e) {
            throw new InvalidTransactionException("Transaction failed: " + e.getMessage());
        }

        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllTransactions();
    }

    public List<Transaction> getClientTransactions(Client client) {
        return transactionRepository.findAllClientTransactions(client);
    }
}

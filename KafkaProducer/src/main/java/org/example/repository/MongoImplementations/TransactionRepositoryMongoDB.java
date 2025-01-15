package org.example.repository.MongoImplementations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.example.model.Client;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionRepositoryMongoDB implements TransactionRepository {

    private final MongoCollection<Transaction> transactionCollection;

    public TransactionRepositoryMongoDB(MongoCollection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
    }

    @Override
    public Transaction findTransactionById(UUID id) {
        return transactionCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionCollection.find().into(new ArrayList<>());
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        transactionCollection.insertOne(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAllClientTransactions(Client client) {
        return transactionCollection.find(Filters.eq("client._id", client.getEntityId())).into(new ArrayList<>());
    }
}

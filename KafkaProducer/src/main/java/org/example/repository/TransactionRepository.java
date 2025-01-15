package org.example.repository;

import org.example.model.Client;
import org.example.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    Transaction findTransactionById(UUID id);
    List<Transaction> findAllTransactions();
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> findAllClientTransactions(Client client);
}

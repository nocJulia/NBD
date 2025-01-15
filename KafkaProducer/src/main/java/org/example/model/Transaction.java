package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Transaction extends AbstractEntity {

    @BsonProperty("transaction_date")
    private LocalDate transactionDate;

    @BsonProperty("end_date")
    private LocalDate endDate; // Dla wynajmu

    @BsonProperty("cost")
    private double finalCost;

    @BsonProperty("client")
    private Client client;

    @BsonProperty("building")
    private Building building;

    @BsonProperty("transaction_type")
    private TransactionType transactionType;

    public Transaction(Client client, Building building, TransactionType transactionType, double finalCost) {
        super(UUID.randomUUID());
        this.client = client;
        this.building = building;
        this.transactionType = transactionType;
        this.transactionDate = LocalDate.now();
        this.finalCost = finalCost;
        if (transactionType == TransactionType.RENT) {
            this.endDate = transactionDate.plusMonths(1); // Domyślny okres wynajmu to 1 miesiąc
        }
        client.addMoneySpent(finalCost);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId()+
                " Client=" + client +
                ", Building=" + building +
                ", finalCost=" + finalCost +
                ", transactionType=" + transactionType +
                '}';
    }

    @BsonCreator
    public Transaction(
            @BsonProperty("transaction_date") LocalDate transactionDate,
            @BsonProperty("end_date") LocalDate endDate,
            @BsonProperty("cost") double finalCost,
            @BsonProperty("client") Client client,
            @BsonProperty("building") Building building,
            @BsonProperty("transaction_type") TransactionType transactionType) {
        this.transactionDate = transactionDate;
        this.endDate = endDate;
        this.finalCost = finalCost;
        this.client = client;
        this.building = building;
        this.transactionType = transactionType;
    }
}

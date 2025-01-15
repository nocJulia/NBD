package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Client extends AbstractEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("email")
    private String email;

    @BsonProperty("phone_number")
    private String phoneNumber;

    @BsonProperty("money_spent")
    private double moneySpent = 0.0;

    @BsonCreator
    public Client(
            @BsonProperty("_id") UUID id,
            @BsonProperty("name") String name,
            @BsonProperty("email") String email,
            @BsonProperty("phone_number") String phoneNumber,
            @BsonProperty("money_spent") double moneySpent){
        super(id);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.moneySpent = moneySpent;
    }

    public Client(String name, String email, String phoneNumber) {
        super(UUID.randomUUID());
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addMoneySpent(double amount) {
        this.moneySpent += amount;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", moneySpent=" + moneySpent +
                '}';
    }
}

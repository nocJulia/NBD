package org.example;

import com.mongodb.MongoException;
import org.example.model.*;
import org.example.model.type.HiddenGem;
import org.example.model.type.LocalType;
import org.example.model.type.PopularLocal;
import org.example.model.type.RegularLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.example.model.TransactionType.SALE;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (Shop shop = new Shop()) {
            Building building = shop.getBuildingManager().registerBuilding("b1", 1000, 20, 3, "b1DESC");
            Building building1 = shop.getBuildingManager().registerBuilding("b2", 2000, 30, 4, "b2DESC");

            LocalType type1 = new RegularLocal();
            LocalType type2 = new HiddenGem();
            LocalType type3 = new PopularLocal();
            Local local1 = shop.getLocalManager().registerLocal("l1", "Lodz", "Ogrodowa", "23", type1);
            Local local2 = shop.getLocalManager().registerLocal("l2", "Lodz", "Gdanska", "256", type2);
            Local local3 = shop.getLocalManager().registerLocal("l3", "Lodz", "Dluga", "3", type3);
            Client client = shop.getClientManager().registerClient("Julia", "Nocun", "334333444");
            Client client2 = shop.getClientManager().registerClient("Tycjan", "Lamkiewicz", "999222111");

            List<BuildingEntry> purchases = new ArrayList<>();
            purchases.add(new BuildingEntry(building, 1));
            purchases.add(new BuildingEntry(building1, 5));

            try {
                // Przetwarzanie transakcji
                Transaction transaction1 = shop.getTransactionManager().processTransaction(client, building, SALE, 1000.00);

                // Po przetworzeniu transakcji, wysyłamy ją do Kafka
                KafkaProducent kafkaProducent = new KafkaProducent(); // Inicjalizacja producenta Kafka
                kafkaProducent.sendTransactionAsync(transaction1); // Wysyłanie transakcji do Kafka
            } catch (MongoException e) {
                e.printStackTrace();
            }

            // Wydrukowanie zawartości baz danych (MongoDB)
            shop.getRepository().getDatabase().getCollection("clients").find().forEach(System.out::println);
            shop.getRepository().getDatabase().getCollection("buildings").find().forEach(System.out::println);
            shop.getRepository().getDatabase().getCollection("transactions").find().forEach(System.out::println);
            shop.getRepository().getDatabase().getCollection("local").find().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

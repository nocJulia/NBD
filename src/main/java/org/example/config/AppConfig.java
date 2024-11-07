//package org.example.config;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(basePackages = "org.example.repository")  // Włącz MongoDB repozytoria
//public class AppConfig extends AbstractMongoClientConfiguration {
//
//    @Override
//    protected String getDatabaseName() {
//        return "your_database_name";  // Nazwa Twojej bazy danych MongoDB
//    }
//
//    @Bean
//    @Override
//    public MongoClient mongoClient() {
//        return MongoClients.create("mongodb://localhost:27017");  // Adres MongoDB
//    }
//
//    @Override
//    protected boolean autoIndexCreation() {
//        return true;  // Automatyczne tworzenie indeksów w MongoDB
//    }
//}

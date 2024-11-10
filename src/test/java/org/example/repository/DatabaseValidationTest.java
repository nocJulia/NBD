package org.example.repository;

import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;
import org.example.validation.MongoSchemaValidator;
import org.example.repository.*;
import org.example.model.*;
import org.example.mappers.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseValidationTest {
    private BudynekRepository budynekRepository;
    private LokalRepository lokalRepository;

    @BeforeEach
    void setUp() {
        budynekRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
        lokalRepository = new LokalRepository(lokalMapper);

        budynekRepository.clearCollection();
        lokalRepository.clearCollection();
    }

    @Test
    void dobryBudynekZapisz() {
        Budynek budynek = new Budynek("TestBuilding");

        assertDoesNotThrow(() -> {
            budynekRepository.save(budynek);
        });
    }

    @Test
    void zlyBudynekZapisz() {
        Budynek budynek = new Budynek("");  // Invalid empty name

        assertThrows(Exception.class, () -> {
            budynekRepository.save(budynek);
        });
    }

    @Test
    void dobryLokalZapisz() {
        Budynek budynek = new Budynek("TestBuilding");
        budynekRepository.save(budynek);
        Biuro biuro = new Biuro(budynek, 100.0, 50.0, 1000.0);

        assertDoesNotThrow(() -> {
            lokalRepository.save(biuro);
        });
    }

    @Test
    void zlyLokalZapisz() {
        Budynek budynek = new Budynek("TestBuilding");
        budynekRepository.save(budynek);
        Biuro biuro = new Biuro(budynek, -100.0, 50.0, 1000.0);

        assertThrows(Exception.class, () -> {
            lokalRepository.save(biuro);
        });
    }
}

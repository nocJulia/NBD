package org.example;

import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.repositories.BudynekCassandraRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BudynekRepositoryTest {

    private BudynekCassandraRepository budynekRepository;
    private UUID budynekId;

    @BeforeEach
    void setUp() {
        budynekRepository = new BudynekCassandraRepository();

        Budynek budynek = new Budynek("Budynek A");
        budynekId = budynek.getId();
        budynekRepository.addBudynek(budynek);
    }

    @Test
    void testAddBudynek() {
        Budynek budynek = new Budynek("Budynek B");
        budynekRepository.addBudynek(budynek);

        Budynek retrievedBudynek = budynekRepository.getBudynek(budynek.getId());
        assertNotNull(retrievedBudynek);
        assertEquals("Budynek B", retrievedBudynek.getNazwa());
    }

    @Test
    void testGetBudynek() {
        Budynek retrievedBudynek = budynekRepository.getBudynek(budynekId);

        assertNotNull(retrievedBudynek);
        assertEquals("Budynek A", retrievedBudynek.getNazwa());
        assertEquals(budynekId, retrievedBudynek.getId());
    }

    @Test
    void testUpdateBudynek() {
        Budynek budynek = budynekRepository.getBudynek(budynekId);
        budynek.setNazwa("Budynek Z");
        budynekRepository.updateBudynek(budynek);

        Budynek updatedBudynek = budynekRepository.getBudynek(budynekId);

        assertNotNull(updatedBudynek);
        assertEquals("Budynek Z", updatedBudynek.getNazwa());
    }

    @Test
    void testDeleteBudynek() {
        Budynek budynek = budynekRepository.getBudynek(budynekId);
        boolean deleted = budynekRepository.deleteBudynek(budynek);

        assertTrue(deleted);

        Budynek deletedBudynek = budynekRepository.getBudynek(budynekId);
        assertNull(deletedBudynek);
    }
}
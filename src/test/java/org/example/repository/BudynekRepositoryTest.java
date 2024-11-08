package org.example.repository;

import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BudynekRepositoryTest {

    private BudynekRepository budynekRepository;
    private LokalRepository lokalRepository;

    @BeforeEach
    public void setup() {
        // Inicjalizacja zależności
        budynekRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
        lokalRepository = new LokalRepository(lokalMapper);

        // Wyczyść kolekcje przed każdym testem
        budynekRepository.clearCollection();
        lokalRepository.clearCollection();

    }


    @Test
    public void testSaveBudynek() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Assertions.assertNotNull(budynekRepository.findById(budynek.getId()), "Budynek powinien zostać zapisany i znaleziony w repozytorium.");
    }

    @Test
    public void testFindById() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Budynek foundBudynek = budynekRepository.findById(budynek.getId());
        Assertions.assertNotNull(foundBudynek, "Budynek powinien być znaleziony na podstawie ID.");
        Assertions.assertEquals(budynek.getId(), foundBudynek.getId(), "Identyfikatory budynków powinny się zgadzać.");
    }

    @Test
    public void testFindAll() {
        Budynek budynek1 = new Budynek(new ObjectId(), "Budynek Testowy 1");
        Budynek budynek2 = new Budynek(new ObjectId(), "Budynek Testowy 2");

        budynekRepository.save(budynek1);
        budynekRepository.save(budynek2);

        List<Budynek> budynki = budynekRepository.findAll();
        Assertions.assertEquals(2, budynki.size(), "Repozytorium powinno zawierać dwa budynki.");
    }

    @Test
    public void testUpdateBudynek() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        budynek.setNazwa("Zaktualizowany Budynek Testowy");
        budynekRepository.update(budynek);

        Budynek updatedBudynek = budynekRepository.findById(budynek.getId());
        Assertions.assertNotNull(updatedBudynek, "Zaktualizowany budynek powinien być znaleziony w repozytorium.");
        Assertions.assertEquals("Zaktualizowany Budynek Testowy", updatedBudynek.getNazwa(), "Nazwa budynku powinna być zaktualizowana.");
    }

    @Test
    public void testDeleteBudynek() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        budynekRepository.delete(budynek);
        Assertions.assertNull(budynekRepository.findById(budynek.getId()), "Budynek powinien być usunięty z repozytorium.");
    }

    @Test
    public void testSize() {
        Budynek budynek1 = new Budynek(new ObjectId(), "Budynek Testowy 1");
        Budynek budynek2 = new Budynek(new ObjectId(), "Budynek Testowy 2");

        budynekRepository.save(budynek1);
        budynekRepository.save(budynek2);

        Assertions.assertEquals(2, budynekRepository.size(), "Repozytorium powinno zawierać dwa dokumenty.");
    }

}

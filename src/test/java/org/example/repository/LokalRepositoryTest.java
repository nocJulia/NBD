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

public class LokalRepositoryTest {

    private LokalRepository lokalRepository;
    private BudynekRepository budynekRepository;

    @BeforeEach
    public void setup() {
        // Inicjalizacja zależności
        budynekRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
        lokalRepository = new LokalRepository(lokalMapper);

        budynekRepository.clearCollection();
        lokalRepository.clearCollection();
    }

    @Test
    public void testSaveLokal() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal = new Mieszkanie(new ObjectId(), budynek, 120, 15.0);
        lokalRepository.save(lokal);

        Assertions.assertNotNull(lokalRepository.findById(lokal.getId()), "Lokal powinien zostać zapisany i znaleziony w repozytorium.");
    }

    @Test
    public void testFindById() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal = new Mieszkanie(new ObjectId(), budynek, 100, 10.0);
        lokalRepository.save(lokal);

        Lokal foundLokal = lokalRepository.findById(lokal.getId());
        Assertions.assertNotNull(foundLokal, "Lokal powinien być znaleziony na podstawie ID.");
        Assertions.assertEquals(lokal.getId(), foundLokal.getId(), "Identyfikatory obiektów powinny się zgadzać.");
    }

    @Test
    public void testFindAll() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal1 = new Mieszkanie(new ObjectId(), budynek, 100, 10.0);
        Lokal lokal2 = new Mieszkanie(new ObjectId(), budynek, 80, 12.5);

        lokalRepository.save(lokal1);
        lokalRepository.save(lokal2);

        List<Lokal> lokale = lokalRepository.findAll();
        Assertions.assertEquals(2, lokale.size(), "Repozytorium powinno zawierać dwa lokale.");
    }

    @Test
    public void testUpdateLokal() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal = new Mieszkanie(new ObjectId(), budynek, 100, 10.0);
        lokalRepository.save(lokal);

        lokal.ustawStawke(15.0);  // Zmiana stawki
        lokalRepository.update(lokal);

        Lokal updatedLokal = lokalRepository.findById(lokal.getId());
        Assertions.assertNotNull(updatedLokal, "Zaktualizowany lokal powinien być znaleziony w repozytorium.");
        Assertions.assertEquals(15.0, updatedLokal.dajStawke(), "Stawka lokalu powinna być zaktualizowana do nowej wartości.");
    }

    @Test
    public void testDeleteLokal() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal = new Mieszkanie(new ObjectId(), budynek, 100, 10.0);
        lokalRepository.save(lokal);

        lokalRepository.delete(lokal);
        Assertions.assertNull(lokalRepository.findById(lokal.getId()), "Lokal powinien być usunięty z repozytorium.");
    }

    @Test
    public void testSize() {
        Budynek budynek = new Budynek(new ObjectId(), "Budynek Testowy");
        budynekRepository.save(budynek);

        Lokal lokal1 = new Mieszkanie(new ObjectId(), budynek, 100, 10.0);
        Lokal lokal2 = new Mieszkanie(new ObjectId(), budynek, 80, 12.5);

        lokalRepository.save(lokal1);
        lokalRepository.save(lokal2);

        Assertions.assertEquals(2, lokalRepository.size(), "Repozytorium powinno zawierać dwa dokumenty.");
    }
}

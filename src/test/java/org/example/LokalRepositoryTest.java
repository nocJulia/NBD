package org.example;

import org.example.model.Biuro;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.repositories.LokalCassandraRepository;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LokalRepositoryTest {

    private LokalCassandraRepository repository;

    @BeforeAll
    void setup() {
        repository = new LokalCassandraRepository();
    }

//    @AfterAll
//    void teardown() {
//        // Opcjonalnie: usuwanie danych testowych lub inne operacje końcowe
//        repository.dropKeyspace();
//    }

    @Test
    void testAddAndRetrieveMieszkanie() {
        UUID id = UUID.randomUUID();
        Mieszkanie mieszkanie = new Mieszkanie(id, 50.0, 2000.0, 3);

        repository.addLokal(mieszkanie);

        Lokal retrieved = repository.getLokal(id);

        assertNotNull(retrieved);
        assertTrue(retrieved instanceof Mieszkanie);
        Mieszkanie retrievedMieszkanie = (Mieszkanie) retrieved;
        assertEquals(mieszkanie.getId(), retrievedMieszkanie.getId());
        assertEquals(mieszkanie.getPowierzchnia_w_metrach(), retrievedMieszkanie.getPowierzchnia_w_metrach());
        assertEquals(mieszkanie.getLiczbaPokoi(), retrievedMieszkanie.getLiczbaPokoi());
    }

    @Test
    void testAddAndRetrieveBiuro() {
        UUID id = UUID.randomUUID();
        Biuro biuro = new Biuro(id, 120.0, 4000.0, 800.0);

        repository.addLokal(biuro);

        Lokal retrieved = repository.getLokal(id);

        assertNotNull(retrieved);
        assertTrue(retrieved instanceof Biuro);
        Biuro retrievedBiuro = (Biuro) retrieved;
        assertEquals(biuro.getId(), retrievedBiuro.getId());
        assertEquals(biuro.getPowierzchnia_w_metrach(), retrievedBiuro.getPowierzchnia_w_metrach());
        assertEquals(biuro.dajKoszty(), retrievedBiuro.dajKoszty());
    }

    @Test
    void testUpdateLokal() {
        // Tworzymy nowy obiekt Mieszkania z unikalnym ID
        UUID id = UUID.randomUUID();
        Mieszkanie mieszkanie = new Mieszkanie(id, 55.0, 2500.0, 4);

        // Dodajemy mieszkanie do bazy
        repository.addLokal(mieszkanie);

        // Modyfikujemy dane mieszkania
        mieszkanie.setPowierzchnia_w_metrach(60.0);
        mieszkanie.setStawka(2700.0);

        // Przechodzimy do aktualizacji
        repository.updateLokal(mieszkanie);

        // Pobieramy zaktualizowane mieszkanie z bazy
        Lokal retrieved = repository.getLokal(id);

        // Sprawdzamy, czy mieszkanie zostało zaktualizowane
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof Mieszkanie);
        Mieszkanie updatedMieszkanie = (Mieszkanie) retrieved;
        assertEquals(60.0, updatedMieszkanie.getPowierzchnia_w_metrach());
        assertEquals(2700.0, updatedMieszkanie.getStawka());
    }


    @Test
    void testDeleteLokal() {
        UUID id = UUID.randomUUID();
        Biuro biuro = new Biuro(id, 100.0, 3500.0, 700.0);

        repository.addLokal(biuro);

        boolean deleted = repository.deleteLokal(biuro);

        assertTrue(deleted);
        Lokal retrieved = repository.getLokal(id);
        assertNull(retrieved);
    }
}

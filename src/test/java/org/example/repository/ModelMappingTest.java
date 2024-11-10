package org.example.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.*;
import org.example.mappers.*;
import org.example.repository.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModelMappingTest {
    private BudynekRepository budynekRepository;
    private LokalRepository lokalRepository;
    private BudynekMapper budynekMapper;
    private LokalMapper lokalMapper;

    @BeforeEach
    void setUp() {
        budynekRepository = new BudynekRepository(null);
        lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
        lokalRepository = new LokalRepository(lokalMapper);
        budynekMapper = new BudynekMapper(lokalMapper);
    }

    @Test
    void testBiuroMapping() {
        Budynek budynek = new Budynek("TestBuilding");
        Biuro biuro = new Biuro(budynek, 100.0, 50.0, 1000.0);

        Document doc = lokalMapper.toDocument(biuro);
        Lokal mappedBiuro = lokalMapper.fromDocument(doc);

        assertTrue(mappedBiuro instanceof Biuro);
        assertEquals(biuro.dajPowierzchnie(), mappedBiuro.dajPowierzchnie());
        assertEquals(biuro.dajStawke(), mappedBiuro.dajStawke());
        assertEquals(((Biuro)biuro).dajKoszty(), ((Biuro)mappedBiuro).dajKoszty());
    }

    @Test
    void testMieszkanieMapping() {
        Budynek budynek = new Budynek("TestBuilding");
        Mieszkanie mieszkanie = new Mieszkanie(budynek, 75.0, 30.0);

        Document doc = lokalMapper.toDocument(mieszkanie);
        Lokal mappedMieszkanie = lokalMapper.fromDocument(doc);

        assertTrue(mappedMieszkanie instanceof Mieszkanie);
        assertEquals(mieszkanie.dajPowierzchnie(), mappedMieszkanie.dajPowierzchnie());
        assertEquals(mieszkanie.dajStawke(), mappedMieszkanie.dajStawke());
    }
}

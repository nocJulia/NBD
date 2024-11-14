package org.example.repository;

import org.bson.Document;
import org.example.mappers.LokalMapper;
import org.example.model.Biuro;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelMappingTest {
    private LokalMapper lokalMapper;

    @BeforeEach
    void setUp() {
        BudynekRepository budynekRepository = new BudynekRepository(null);
        lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
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
        assertEquals(biuro.dajKoszty(), ((Biuro)mappedBiuro).dajKoszty());
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

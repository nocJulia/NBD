package org.example;

import org.example.model.Biuro;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BudynekTest {

    @Test
    void testConstructor() {
        Budynek budynek = new Budynek("Budynek A");

        assertNotNull(budynek);
        assertEquals("Budynek A", budynek.getNazwa());
        assertNotNull(budynek.getLokale());
        assertTrue(budynek.getLokale().isEmpty());
    }

    @Test
    void testDodajLokal() {
        Budynek budynek = new Budynek("Budynek A");
        Lokal lokal = new Biuro(UUID.randomUUID(), 100.0, 30.0, 50.0);

        budynek.dodajLokal(lokal);

        assertFalse(budynek.getLokale().isEmpty());
        assertEquals(1, budynek.getLokale().size());
    }

    @Test
    void testSetLokaleJson() {
        Budynek budynek = new Budynek("Budynek A");
        String json = "[{\"id\":\"" + UUID.randomUUID() + "\", \"powierzchnia_w_metrach\":50.0, \"stawka\":20.0, \"typ\":\"Mieszkanie\"}]";

        budynek.setLokaleJson(json);

        assertFalse(budynek.getLokale().isEmpty());
        assertEquals(1, budynek.getLokale().size());
    }
}

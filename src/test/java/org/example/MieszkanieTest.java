package org.example;

import org.example.model.Mieszkanie;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MieszkanieTest {

    @Test
    void testConstructor() {
        UUID id = UUID.randomUUID();
        Mieszkanie mieszkanie = new Mieszkanie(id, 80.0, 25.0, 3);

        assertNotNull(mieszkanie);
        assertEquals(id, mieszkanie.getId());
        assertEquals(80.0, mieszkanie.getPowierzchnia_w_metrach());
        assertEquals(25.0, mieszkanie.getStawka());
        assertEquals(3, mieszkanie.getLiczbaPokoi());
    }

    @Test
    void testCzynsz() {
        Mieszkanie mieszkanie = new Mieszkanie(UUID.randomUUID(), 80.0, 25.0, 3);
        double expectedCzynsz = 80.0 * 25.0;
        assertEquals(expectedCzynsz, mieszkanie.czynsz());
    }
}

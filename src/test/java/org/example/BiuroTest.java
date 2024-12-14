package org.example;

import org.example.model.Biuro;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BiuroTest {

    @Test
    void testConstructor() {
        UUID id = UUID.randomUUID();
        Biuro biuro = new Biuro(id, 100.0, 30.0, 50.0);

        assertNotNull(biuro);
        assertEquals(id, biuro.getId());
        assertEquals(100.0, biuro.getPowierzchnia_w_metrach());
        assertEquals(30.0, biuro.getStawka());
        assertEquals(50.0, biuro.getKosztyDodatkowe());
    }

    @Test
    void testCzynsz() {
        Biuro biuro = new Biuro(UUID.randomUUID(), 100.0, 30.0, 50.0);
        double expectedCzynsz = 100.0 * 30.0 + 50.0;
        assertEquals(expectedCzynsz, biuro.czynsz());
    }

    @Test
    void testKosztyDodatkowe() {
        Biuro biuro = new Biuro();
        biuro.ustawKoszty(60.0);
        assertEquals(60.0, biuro.getKosztyDodatkowe());
    }
}

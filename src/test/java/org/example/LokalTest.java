package org.example;

import org.example.model.Mieszkanie;
import org.example.model.Biuro;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LokalTest {

    @Test
    void testCzynszDlaMieszkania() {
        Mieszkanie mieszkanie = new Mieszkanie(UUID.randomUUID(), 50, 20, 3);
        double expectedCzynsz = 50 * 20;
        assertEquals(expectedCzynsz, mieszkanie.czynsz(), "Czynsz dla mieszkania jest niepoprawny.");
    }

    @Test
    void testCzynszDlaBiura() {
        Biuro biuro = new Biuro(UUID.randomUUID(), 100, 15, 500);
        double expectedCzynsz = (100 * 15) + 500;
        assertEquals(expectedCzynsz, biuro.czynsz(), "Czynsz dla biura jest niepoprawny.");
    }

    @Test
    void testInformacjaMieszkania() {
        Mieszkanie mieszkanie = new Mieszkanie(UUID.randomUUID(), 60, 25, 2);
        String info = mieszkanie.informacja();
        assertTrue(info.contains("Mieszkanie"), "Informacja powinna zawierać typ 'Mieszkanie'.");
    }

    @Test
    void testUstawKosztyDodatkoweBiuro() {
        Biuro biuro = new Biuro(UUID.randomUUID(), 80, 10, 300);
        biuro.ustawKoszty(400);
        assertEquals(400, biuro.dajKoszty(), "Koszty dodatkowe w biurze powinny zostać zaktualizowane.");
    }
}

package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudynekTest {

    private Budynek budynek;
    private Biuro biuro1;
    private Mieszkanie mieszkanie1;

    @BeforeEach
    void setUp() {
        // Przygotowanie instancji Budynku oraz różnych typów lokali przed każdym testem
        budynek = new Budynek("Kompleks");
        biuro1 = new Biuro(budynek, 50, 20, 100); // powierzchnia 50, stawka 20, koszty dodatkowe 100
        mieszkanie1 = new Mieszkanie(budynek, 70, 15); // powierzchnia 70, stawka 15
    }

    @Test
    void testDodajLokal() {
        // Dodajemy lokale do budynku i sprawdzamy, czy zostały poprawnie dodane
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(mieszkanie1);

        List<Lokal> lokale = budynek.getLokale();
        assertEquals(2, lokale.size());
        assertTrue(lokale.contains(biuro1));
        assertTrue(lokale.contains(mieszkanie1));
    }

    @Test
    void testCzynszCalkowity() {
        // Dodajemy lokale do budynku i obliczamy łączny czynsz
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(mieszkanie1);

        double czynszBiuro = biuro1.czynsz(); // (50 * 20) + 100 = 1100
        double czynszMieszkanie = mieszkanie1.czynsz(); // 70 * 15 = 1050
        double czynszCalkowity = budynek.czynszCalkowity();

        assertEquals(czynszBiuro + czynszMieszkanie, czynszCalkowity, 0.01);
    }

    @Test
    void testZysk() {
        // Dodajemy lokale i obliczamy zysk przy wydatkach za metr kwadratowy
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(mieszkanie1);

        double wydatkiZaMetr = 10;
        double oczekiwanyZysk = (biuro1.czynsz() - biuro1.dajPowierzchnie() * wydatkiZaMetr) +
                (mieszkanie1.czynsz() - mieszkanie1.dajPowierzchnie() * wydatkiZaMetr);
        double zysk = budynek.zysk(wydatkiZaMetr);

        assertEquals(Math.max(oczekiwanyZysk, 0), zysk, 0.01);
    }

    @Test
    void testZyskNieujemny() {
        // Dodajemy tylko biuro, gdzie wydatki przewyższają czynsz, zysk powinien być nieujemny
        budynek.dodajLokal(biuro1);

        double zysk = budynek.zysk(50); // Przy wydatkach 50 za metr, zysk nie powinien być ujemny
        assertEquals(0, zysk, 0.01);
    }

    @Test
    void testInformacjaZbiorcza() {
        // Dodajemy lokale i sprawdzamy zbiorcze informacje
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(mieszkanie1);

        String informacja = budynek.informacjaZbiorcza();
        assertTrue(informacja.contains("[Biuro] Powierzchnia w metrach: 50.0 Stawka: 20.0 Koszty dodatkowe: 100.0"));
        assertTrue(informacja.contains("[Mieszkanie] Powierzchnia w metrach: 70.0 Stawka: 15.0"));
    }

    @Test
    void testGetId() {
        // Sprawdzamy, czy metoda getId zwraca poprawne _id
        Budynek nowyBudynek = new Budynek("Magazyn");
        assertNotNull(nowyBudynek.getId());
    }

    @Test
    void testGetNazwa() {
        // Testujemy getNazwa oraz setNazwa
        assertEquals("Kompleks", budynek.getNazwa());

        budynek.setNazwa("Nowy Kompleks");
        assertEquals("Nowy Kompleks", budynek.getNazwa());
    }

    @Test
    void testSetLokale() {
        // Testujemy setLokale i sprawdzamy, czy lista lokali jest poprawnie ustawiona
        List<Lokal> noweLokale = new ArrayList<>();
        noweLokale.add(biuro1);
        budynek.setLokale(noweLokale);

        assertEquals(1, budynek.getLokale().size());
        assertEquals(biuro1, budynek.getLokale().get(0));
    }
}

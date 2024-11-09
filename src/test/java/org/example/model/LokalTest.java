package org.example.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LokalTest {

    private static class TestLokal extends Lokal {
        public TestLokal(Budynek budynek, double powierzchnia, double stawka) {
            super(budynek, powierzchnia, stawka);
        }

        @Override
        public double czynsz() {
            return dajPowierzchnie() * dajStawke();
        }
    }

    @Test
    void testUstawianieIPobieraniePol() {
        Budynek budynek = new Budynek();
        TestLokal lokal = new TestLokal(budynek, 50.0, 20.0);

        Assertions.assertEquals(50.0, lokal.dajPowierzchnie());
        Assertions.assertEquals(20.0, lokal.dajStawke());
        Assertions.assertEquals(budynek, lokal.getBudynek());
    }

    @Test
    void testCzynsz() {
        TestLokal lokal = new TestLokal(null, 50.0, 20.0);
        Assertions.assertEquals(1000.0, lokal.czynsz());
    }
}

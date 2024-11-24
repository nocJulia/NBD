package org.example;

import org.example.model.Budynek;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BudynekTest {

    @Test
    public void testBudynekBrakLokali_ZerowyCzynsz() {
        Budynek budynek = new Budynek();
        assertEquals(0.0, budynek.czynszCalkowity());
    }

    @Test
    public void testBudynekBrakLokaliMaleKoszty_BrakZysku() {
        Budynek budynek = new Budynek();
        assertEquals(0.0, budynek.zysk(1.0));
    }

    @Test
    public void testBudynekBrakLokaliBrakKosztow_NadalBrakZysku() {
        Budynek budynek = new Budynek();
        assertEquals(0.0, budynek.zysk(0.0));
    }
}

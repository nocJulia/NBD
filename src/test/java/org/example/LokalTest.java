package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LokalTest {

    @Test
    public void testBudynekCzynszCalkowity_PiecLokali() {
        Budynek budynek = new Budynek();
        Mieszkanie mieszkanie1 = new Mieszkanie(60.0, 15.0);
        Mieszkanie mieszkanie2 = new Mieszkanie(90.0, 14.0);
        Mieszkanie mieszkanie3 = new Mieszkanie(40.0, 10.0);
        Biuro biuro1 = new Biuro(20.0, 20.0, 140.0);
        Biuro biuro2 = new Biuro(90.0, 30.0, 200.0);

        budynek.dodajLokal(mieszkanie1);
        budynek.dodajLokal(mieszkanie2);
        budynek.dodajLokal(mieszkanie3);
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(biuro2);

        assertEquals(6000.0, budynek.czynszCalkowity());
    }

    @Test
    public void testBudynekZysk_PiecLokali() {
        Budynek budynek = new Budynek();
        Mieszkanie mieszkanie1 = new Mieszkanie(60.0, 15.0);
        Mieszkanie mieszkanie2 = new Mieszkanie(90.0, 14.0);
        Mieszkanie mieszkanie3 = new Mieszkanie(40.0, 10.0);
        Biuro biuro1 = new Biuro(20.0, 20.0, 140.0);
        Biuro biuro2 = new Biuro(90.0, 30.0, 200.0);

        budynek.dodajLokal(mieszkanie1);
        budynek.dodajLokal(mieszkanie2);
        budynek.dodajLokal(mieszkanie3);
        budynek.dodajLokal(biuro1);
        budynek.dodajLokal(biuro2);

        assertEquals(3600.0, budynek.zysk(8.0));
    }
}

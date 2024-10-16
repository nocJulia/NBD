package org.example.service;

import org.example.model.Budynek;
import org.example.model.Lokal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudynekServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BudynekService budynekService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void dodajLokalDoBudynku_OptimisticLockException() {
        Budynek budynek = new Budynek("Budynek1");
        Lokal lokal = mock(Lokal.class);

        doThrow(OptimisticLockException.class).when(entityManager).persist(any(Budynek.class));

        assertThrows(OptimisticLockException.class, () -> budynekService.dodajLokalDoBudynku(budynek, lokal));
    }

    @Test
    void znajdzBudynekTest() {
        Long id = 1L;
        Budynek expectedBudynek = new Budynek("Budynek1");
        when(entityManager.find(Budynek.class, id)).thenReturn(expectedBudynek);

        Budynek result = budynekService.znajdzBudynek(id);

        assertEquals(expectedBudynek, result);
    }

    @Test
    void aktualizujCzynszTest() {
        Lokal lokal = mock(Lokal.class);
        double nowaStawka = 1000.0;

        budynekService.aktualizujCzynsz(lokal, nowaStawka);

        verify(lokal).ustawStawke(nowaStawka);
        verify(entityManager).merge(lokal);
    }

    @Test
    void aktualizujCzynszCzyDaSie() {
        Lokal lokal = mock(Lokal.class);
        double nowaStawka = 1000.0;

        doThrow(OptimisticLockException.class).when(entityManager).merge(any(Lokal.class));

        assertThrows(OptimisticLockException.class, () -> budynekService.aktualizujCzynsz(lokal, nowaStawka));
    }

    @Test
    void usunLokalCzyDaSie() {
        Lokal lokal = mock(Lokal.class);
        when(entityManager.contains(lokal)).thenReturn(true);

        budynekService.usunLokal(lokal);

        verify(entityManager).remove(lokal);
    }

    @Test
    void usunLokalCzyNieDaSie() {
        Lokal lokal = mock(Lokal.class);
        when(entityManager.contains(lokal)).thenReturn(false);
        when(entityManager.merge(lokal)).thenReturn(lokal);

        budynekService.usunLokal(lokal);

        verify(entityManager).merge(lokal);
        verify(entityManager).remove(lokal);
    }
}
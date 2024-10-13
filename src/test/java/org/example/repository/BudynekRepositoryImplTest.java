package org.example.repository;

import org.example.model.Budynek;
import org.example.repository.impl.BudynekRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BudynekRepositoryImplTest {

    private EntityManager entityManager;
    private BudynekRepositoryImpl budynekRepository;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        budynekRepository = new BudynekRepositoryImpl();
        // Ręczne wstrzyknięcie mockowanego EntityManager do repozytorium
        ReflectionTestUtils.setField(budynekRepository, "em", entityManager);
    }

    @Test
    void testSave() {
        Budynek budynek = new Budynek("Budynek A");

        budynekRepository.save(budynek);

        verify(entityManager).persist(budynek);
    }

    @Test
    void testFindById() {
        Budynek budynek = new Budynek("Budynek A");
        budynek.setId(1L);
        when(entityManager.find(Budynek.class, 1L)).thenReturn(budynek);

        Budynek foundBudynek = budynekRepository.findById(1L);

        assertEquals(budynek, foundBudynek);
    }

    @Test
    void testFindAll() {
        List<Budynek> budynki = List.of(new Budynek("Budynek A"), new Budynek("Budynek B"));
        TypedQuery<Budynek> query = mockQuery(budynki);
        when(entityManager.createQuery("SELECT b FROM Budynek b", Budynek.class)).thenReturn(query);

        List<Budynek> result = budynekRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(budynki, result);
    }

    @Test
    void testDelete() {
        Budynek budynek = new Budynek("Budynek A");
        budynek.setId(1L);
        when(entityManager.contains(budynek)).thenReturn(true);

        budynekRepository.delete(budynek);

        verify(entityManager).remove(budynek);
    }

    @Test
    void testDeleteNonExisting() {
        Budynek budynek = new Budynek("Budynek A");
        budynek.setId(1L);
        when(entityManager.contains(budynek)).thenReturn(false);
        when(entityManager.find(Budynek.class, 1L)).thenReturn(budynek);

        budynekRepository.delete(budynek);

        verify(entityManager).remove(budynek);
    }

    private <T> TypedQuery<T> mockQuery(List<T> resultList) {
        TypedQuery<T> query = Mockito.mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(resultList);
        return query;
    }
}

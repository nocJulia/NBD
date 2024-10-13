package org.example.repository;

import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.repository.impl.LokalRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LokalRepositoryImplTest {

    private EntityManager entityManager;
    private LokalRepositoryImpl lokalRepository;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        lokalRepository = new LokalRepositoryImpl();
        lokalRepository.setEntityManager(entityManager);
    }

    @Test
    void testSave() {
        Lokal lokal = new Mieszkanie(50, 20);

        lokalRepository.save(lokal);

        verify(entityManager).persist(lokal);
    }

    @Test
    void testDelete() {
        Lokal lokal = new Mieszkanie(50, 20);

        // Umożliwienie, by mockował, że lokal jest zawarty w entityManager
        when(entityManager.contains(lokal)).thenReturn(true);

        lokalRepository.delete(lokal);

        verify(entityManager).remove(lokal);
    }

    @Test
    void testFindAll() {
        List<Lokal> lokals = List.of(new Mieszkanie(50, 20), new Mieszkanie(30, 15));

        // Mockowanie zapytania
        TypedQuery<Lokal> query = mockQuery(lokals);

        when(entityManager.createQuery("SELECT l FROM Lokal l", Lokal.class)).thenReturn(query);

        List<Lokal> result = lokalRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(lokals, result);
    }

    @Test
    void testFindById() {
        Lokal lokal = new Mieszkanie(50, 20);
        when(entityManager.find(Lokal.class, 1L)).thenReturn(lokal);

        Lokal foundLokal = lokalRepository.findById(1L);

        assertEquals(lokal, foundLokal);
    }

    private <T> TypedQuery<T> mockQuery(List<T> resultList) {
        TypedQuery<T> query = Mockito.mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(resultList);
        return query;
    }

}

package org.example.service;

import jakarta.persistence.OptimisticLockException;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BudynekService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void dodajLokalDoBudynku(Budynek budynek, Lokal lokal) {
        try {
            budynek.dodajLokal(lokal);
            em.persist(budynek);
        } catch (OptimisticLockException e) {
            // Obsługa błędu
            System.out.println("Operacja nieudana z powodu równoczesnej modyfikacji danych. Spróbuj ponownie.");
            throw e;
        }
    }

    // Poziom izolacji REPEATABLE_READ
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Budynek znajdzBudynek(Long id) {
        return em.find(Budynek.class, id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void aktualizujCzynsz(Lokal lokal, double nowaStawka) {
        try {
            lokal.ustawStawke(nowaStawka);
            em.merge(lokal);
        } catch (OptimisticLockException e) {
            // Obsługa błędu
            System.out.println("Aktualizacja czynszu nie powiodła się z powodu równoczesnej modyfikacji.");
            throw e;
        }
    }

    // Poziom izolacji READ_UNCOMMITTED (najniższy poziom izolacji)
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void usunLokal(Lokal lokal) {

        em.remove(em.contains(lokal) ? lokal : em.merge(lokal));
    }

}

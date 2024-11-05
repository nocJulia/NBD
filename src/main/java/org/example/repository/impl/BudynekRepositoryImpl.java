//package org.example.repository.impl;
//
//import org.example.model.Budynek;
//import org.example.repository.BudynekRepository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//
//public class BudynekRepositoryImpl implements BudynekRepository {
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public void save(Budynek budynek) {
//        em.persist(budynek);
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true) // Poziom izolacji dla odczytu
//    public Budynek findById(Long id) {
//        return em.find(Budynek.class, id);
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true) // Poziom izolacji dla odczytu
//    public List<Budynek> findAll() {
//        return em.createQuery("SELECT b FROM Budynek b", Budynek.class).getResultList();
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED) // Poziom izolacji dla operacji usuwania
//    public void delete(Budynek budynek) {
//        if (em.contains(budynek)) {
//            em.remove(budynek);
//        } else {
//            Budynek managed = findById(budynek.getId());
//            if (managed != null) {
//                em.remove(managed);
//            }
//        }
//    }
//}
//
//

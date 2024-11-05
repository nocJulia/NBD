//package org.example.repository.impl;
//
//import org.example.model.Lokal;
//import org.example.repository.LokalRepository;
//
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.EntityManager;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//public class LokalRepositoryImpl implements LokalRepository{
//
//    private EntityManager entityManager;
//
//    // Setter do wstrzykiwania EntityManager
//    public void setEntityManager(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public void save(Lokal lokal) {
//        entityManager.persist(lokal);  // Zapisuje nowy lokal do bazy danych
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public void delete(Lokal lokal) {
//        if (entityManager.contains(lokal)) {
//            entityManager.remove(lokal);  // Usuwa lokal, jeśli jest zarządzany przez EntityManager
//        } else {
//            Lokal managed = findById(lokal.getId());
//            if (managed != null) {
//                entityManager.remove(managed);  // Usuwa lokal, jeśli nie jest zarządzany
//            }
//        }
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
//    public List<Lokal> findAll() {
//        TypedQuery<Lokal> query = entityManager.createQuery("SELECT l FROM Lokal l", Lokal.class);
//        return query.getResultList();  // Zwraca listę wszystkich lokali
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
//    public Lokal findById(Long id) {
//        return entityManager.find(Lokal.class, id);  // Znajduje lokal po ID
//    }
//}
//
//

//package org.example.repository;
//
//import org.example.model.Lokal;
//
//import java.util.List;
//
//public interface LokalRepository {
//    void save(Lokal lokal);
//    Lokal findById(Long id);
//    List<Lokal> findAll();
//    void delete(Lokal lokal);
//}

package org.example.repository;

import org.example.model.Budynek;
import org.example.model.Lokal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LokalRepository extends MongoRepository<Lokal, String> {
    Optional<Lokal> findById(String id);
}

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

import org.example.model.Lokal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LokalRepository extends MongoRepository<Lokal, String> {
}

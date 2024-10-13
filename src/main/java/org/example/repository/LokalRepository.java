package org.example.repository;

import org.example.model.Lokal;

import java.util.List;

public interface LokalRepository {
    void save(Lokal lokal);
    Lokal findById(Long id);
    List<Lokal> findAll();
    void delete(Lokal lokal);
}

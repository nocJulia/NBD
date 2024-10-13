package org.example.repository;

import org.example.model.Budynek;

import java.util.List;

public interface BudynekRepository {
    void save(Budynek budynek);
    Budynek findById(Long id);
    List<Budynek> findAll();
    void delete(Budynek budynek);
}

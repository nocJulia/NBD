package org.example.repository;

import java.util.List;

public interface Repository<T> {
    void save(T obj);
    T findById(String id);
    List<T> findAll();
    void update(T obj);
    void delete(T obj);
    int size();
}

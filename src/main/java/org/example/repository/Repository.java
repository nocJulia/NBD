package org.example.repository;

import org.bson.types.ObjectId;

import java.util.List;

public interface Repository<T> {
    void save(T obj);
    T findById(ObjectId id);
    List<T> findAll();
    void update(T obj);
    void delete(T obj);
    int size();
}

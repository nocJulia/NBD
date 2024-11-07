package org.example.mappers;

import org.bson.Document;

public interface Mapper<T> {
    Document toDocument(T entity);
    T fromDocument(Document document);
}

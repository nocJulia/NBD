package org.example.exceptions;

import com.mongodb.MongoException;

public class MongoDBException  extends MongoException {
    public MongoDBException(String errorWhileCreatingDatabase) {
        super(errorWhileCreatingDatabase);
    }
}
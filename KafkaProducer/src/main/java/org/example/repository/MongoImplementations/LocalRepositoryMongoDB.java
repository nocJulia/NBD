package org.example.repository.MongoImplementations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.example.model.Local;
import org.example.repository.LocalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocalRepositoryMongoDB implements LocalRepository {

    private final MongoCollection<Local> localsCollection;

    public LocalRepositoryMongoDB(MongoCollection<Local> localsCollection) {
        this.localsCollection = localsCollection;
    }

    @Override
    public Local saveLocal(Local local) {
        localsCollection.insertOne(local);
        return local;
    }

    @Override
    public boolean deleteLocal(UUID id) {
        Local deletedLocal = localsCollection.findOneAndDelete(Filters.eq("_id", id));
        return deletedLocal != null;
    }

    @Override
    public Local findLocalById(UUID id) {
        return localsCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Local> findAllLocals() {
        return localsCollection.find().into(new ArrayList<>());
    }
}

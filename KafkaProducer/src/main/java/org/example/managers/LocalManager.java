package org.example.managers;

import com.mongodb.client.MongoCollection;
import org.example.model.Address;
import org.example.model.Local;
import org.example.model.type.LocalType;
import org.example.repository.LocalRepository;
import org.example.repository.MongoImplementations.LocalRepositoryMongoDB;

import java.util.List;
import java.util.UUID;

public class LocalManager {
    private final LocalRepository localRepository;

    public LocalManager(MongoCollection<Local> collection) {
        this.localRepository = new LocalRepositoryMongoDB(collection);
    }

    public Local getLocal(UUID id) {
        return localRepository.findLocalById(id);
    }

    public Local registerLocal(String name, String city, String street, String number, LocalType localType) {
        Address address = new Address(city, street, number);
        Local local = new Local(name, address, localType);
        return localRepository.saveLocal(local);
    }

    public List<Local> getAllLocals() {
        return localRepository.findAllLocals();
    }
}

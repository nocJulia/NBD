package org.example.managers;

import org.example.model.Lokal;
import org.example.dao.LokalDao;

import java.util.Optional;
import java.util.UUID;

public class LokalManager {
    private final LokalDao lokalDao;

    public LokalManager(LokalDao lokalDao) {
        this.lokalDao = lokalDao;
    }

    public void saveLokal(Lokal lokal) {
        if (lokal.getId() == null) {
            lokal.setId(UUID.randomUUID());
        }
        lokalDao.save(lokal);
    }

    public Optional<Lokal> getLokalById(UUID id) {
        return Optional.ofNullable(lokalDao.findById(id));
    }

//    public void updateLokal(Lokal lokal) {
//        lokalDao.update(lokal);
//    }

    public boolean deleteLokal(Lokal lokal) {
        return lokalDao.delete(lokal);
    }
}
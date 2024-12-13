package org.example.managers;

import org.example.model.Budynek;
import org.example.dao.BudynekDao;

import java.util.Optional;
import java.util.UUID;

public class BudynekManager {
    private final BudynekDao budynekDao;

    public BudynekManager(BudynekDao budynekDao) {
        this.budynekDao = budynekDao;
    }

    public void addBudynek(Budynek budynek) {
        if (budynek.getId() == null) {
            budynek.setId(UUID.randomUUID());
        }
        budynekDao.save(budynek);
    }

    public Optional<Budynek> getBudynekById(UUID id) {
        return Optional.ofNullable(budynekDao.findById(id));
    }

    public void updateBudynek(Budynek budynek) {
        budynekDao.update(budynek);
    }

    public boolean deleteBudynek(Budynek budynek) {
        return budynekDao.delete(budynek);
    }
}
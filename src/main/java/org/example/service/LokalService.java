package org.example.service;

import org.example.model.Lokal;
import org.example.repository.LokalRepository;

import jakarta.transaction.Transactional;
import java.util.List;

public class LokalService {
    private final LokalRepository lokalRepository;

    public LokalService(LokalRepository lokalRepository) {
        this.lokalRepository = lokalRepository;
    }

    @Transactional
    public void saveLokal(Lokal lokal) {
        if (lokal == null) {
            throw new IllegalArgumentException("Lokal cannot be null");
        }
        lokalRepository.save(lokal);
    }

    public Lokal findLokalById(Long id) {
        return lokalRepository.findById(id);
    }

    public List<Lokal> findAllLokale() {
        return lokalRepository.findAll();
    }

    @Transactional
    public void deleteLokal(Lokal lokal) {
        if (lokal == null) {
            throw new IllegalArgumentException("Lokal cannot be null");
        }
        lokalRepository.delete(lokal);
    }
}

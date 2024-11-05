package org.example.service;

import org.example.model.Lokal;
import org.example.repository.LokalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LokalService {

    private final LokalRepository lokalRepository;

    @Autowired
    public LokalService(LokalRepository lokalRepository) {
        this.lokalRepository = lokalRepository;
    }

    // Zapisz nowy lokal w MongoDB
    public void saveLokal(Lokal lokal) {
        if (lokal == null) {
            throw new IllegalArgumentException("Lokal cannot be null");
        }
        lokalRepository.save(lokal);  // Zapisz lokal
    }

    // Znajdź lokal po identyfikatorze (String)
    public Optional<Lokal> findLokalById(String id) {
        return lokalRepository.findById(id);  // Znajdź lokal na podstawie ID
    }

    // Znajdź wszystkie lokale
    public List<Lokal> findAllLokale() {
        return lokalRepository.findAll();  // Zwróć wszystkie lokale
    }

    // Usuń lokal z MongoDB
    public void deleteLokal(Lokal lokal) {
        if (lokal == null) {
            throw new IllegalArgumentException("Lokal cannot be null");
        }
        lokalRepository.delete(lokal);  // Usuń lokal
    }

    // Usuń lokal na podstawie ID
    public void deleteLokalById(String id) {
        lokalRepository.deleteById(id);  // Usuń lokal po ID
    }

    // Aktualizuj istniejący lokal
    public void updateLokal(String id, Lokal updatedLokal) {
        if (id == null || updatedLokal == null) {
            throw new IllegalArgumentException("ID and Lokal cannot be null");
        }

        // Sprawdź, czy lokal o podanym ID istnieje
        Optional<Lokal> existingLokalOpt = lokalRepository.findById(id);
        if (existingLokalOpt.isPresent()) {
            Lokal existingLokal = existingLokalOpt.get();

            // Aktualizuj właściwości lokalu na podstawie wartości z updatedLokal
            existingLokal.ustawPowierzchnie(updatedLokal.dajPowierzchnie());
            existingLokal.ustawStawke(updatedLokal.dajStawke());
            existingLokal.setBudynek(updatedLokal.getBudynek());  // Zakładając, że można aktualizować budynek

            // Zapisz zaktualizowany lokal
            lokalRepository.save(existingLokal);
        } else {
            throw new IllegalArgumentException("Lokal with ID " + id + " not found");
        }
    }
}

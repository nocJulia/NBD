package org.example.service;

import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.repository.BudynekRepository;
import org.example.repository.LokalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudynekService {

    private final BudynekRepository budynekRepository;
    private final LokalRepository lokalRepository;

    @Autowired
    public BudynekService(BudynekRepository budynekRepository, LokalRepository lokalRepository) {
        this.budynekRepository = budynekRepository;
        this.lokalRepository = lokalRepository;
    }

    // Dodaj lokal do budynku i zapisz do MongoDB
    public void dodajLokalDoBudynku(String budynekId, Lokal lokal) {
        Optional<Budynek> budynekOpt = budynekRepository.findById(budynekId);
        if (budynekOpt.isPresent()) {
            Budynek budynek = budynekOpt.get();
            budynek.dodajLokal(lokal);  // Dodajemy lokal do budynku
            budynekRepository.save(budynek);  // Zapisujemy zaktualizowany budynek
        } else {
            System.out.println("Nie znaleziono budynku o podanym ID.");
        }
    }

    // Znajdź budynek po ID
    public Optional<Budynek> znajdzBudynek(String id) {
        return budynekRepository.findById(id);
    }

    // Aktualizacja stawki czynszu dla lokalu
    public void aktualizujCzynsz(Lokal lokal, double nowaStawka) {
        lokal.ustawStawke(nowaStawka);
        lokalRepository.save(lokal);  // Aktualizacja i zapis w MongoDB
    }

    // Usuwanie lokalu
    public void usunLokal(String lokalId) {
        lokalRepository.deleteById(lokalId);  // Usunięcie lokalu z MongoDB
    }

    // Metoda do aktualizacji budynku
    public void aktualizujBudynek(String budynekId, Budynek nowyBudynek) {
        Optional<Budynek> budynekOpt = budynekRepository.findById(budynekId);
        if (budynekOpt.isPresent()) {
            Budynek istniejącyBudynek = budynekOpt.get();

            // Aktualizuj pola istniejącego budynku
            istniejącyBudynek.setNazwa(nowyBudynek.getNazwa());
            istniejącyBudynek.setLokale(nowyBudynek.getLokale());

            // Zapisz zaktualizowany budynek
            budynekRepository.save(istniejącyBudynek);
        } else {
            System.out.println("Nie znaleziono budynku o podanym ID.");
        }
    }
}


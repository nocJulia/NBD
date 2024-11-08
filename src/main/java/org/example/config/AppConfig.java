package org.example.config;

import org.example.repository.BudynekRepository;
import org.example.repository.LokalRepository;
import org.example.mappers.LokalMapper;

public class AppConfig {

    public BudynekRepository createBudynekRepository() {
        // Tworzymy instancję BudynekRepository bez LokalMapper
        BudynekRepository budynekRepository = new BudynekRepository(null);

        // Tworzymy instancję LokalMapper, przekazując BudynekRepository jako zależność
        LokalMapper lokalMapper = new LokalMapper(budynekRepository);

        // Przypisujemy lokalMapper do BudynekRepository (jeśli BudynekMapper potrzebuje tej zależności)
        budynekRepository.setLokalMapper(lokalMapper);

        // Tworzymy instancję LokalRepository, przekazując lokalMapper
        LokalRepository lokalRepository = new LokalRepository(lokalMapper);

        return budynekRepository;  // lub zwróć oba, jeśli będą potrzebne
    }
}

package org.example.dao;


import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Budynek;
import org.example.queryProviders.BudynekQueryProvider;

import java.util.UUID;

@Dao
public interface BudynekDao {

//    QUORUM dla zapisu i odczytu: Jest to dobry kompromis między dostępnością a spójnością.
//    Dzięki temu operacje zapisu i odczytu są dobrze rozdystrybuowane w klastrze, zapewniając,
//    że dane są dostępne, ale również odpowiednio spójne.
//    ONE dla usuwania: Zapewnienie jak najwyższej wydajności dla operacji usuwania, gdzie spójność
//    nie jest aż tak krytyczna (możliwość, że operacja usunięcia będzie zsynchronizowana później).

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BudynekQueryProvider.class)
    void save(Budynek budynek);
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BudynekQueryProvider.class)
    void update(Budynek budynek);
    @StatementAttributes(consistencyLevel = "ONE")
    @Delete
    boolean delete (Budynek budynek);
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Budynek findById(UUID id);

}

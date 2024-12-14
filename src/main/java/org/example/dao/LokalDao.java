package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Biuro;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.queryProviders.LokalQueryProvider;

import java.util.UUID;

@Dao
public interface LokalDao {

//    QUORUM dla zapisu i odczytu: Jest to dobry kompromis między dostępnością a spójnością.
//    Dzięki temu operacje zapisu i odczytu są dobrze rozdystrybuowane w klastrze, zapewniając,
//    że dane są dostępne, ale również odpowiednio spójne.
//    ONE dla usuwania: Zapewnienie jak najwyższej wydajności dla operacji usuwania, gdzie spójność
//    nie jest aż tak krytyczna (możliwość, że operacja usunięcia będzie zsynchronizowana później).

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = LokalQueryProvider.class,
            entityHelpers = {Mieszkanie.class, Biuro.class})
    Lokal save(Lokal lokal);

    @StatementAttributes(consistencyLevel = "ONE")
    @Delete
    boolean delete(Lokal lokal);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = LokalQueryProvider.class,
            entityHelpers = {Mieszkanie.class, Biuro.class})
    Lokal findById(UUID id);
}

package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Biuro;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.queryProviders.LokalQueryProvider;

import java.util.UUID;

@Dao
public interface LokalDao {

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

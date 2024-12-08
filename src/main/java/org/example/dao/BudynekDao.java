package org.example.dao;


import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.mapper.annotations.*;
import jnr.ffi.annotations.In;
import org.example.model.Budynek;
import org.example.queryProviders.BudynekQueryProvider;


import java.util.UUID;

@Dao
public interface BudynekDao {

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


//    @Delete(entityClass = RentDB.class)
//    void deleteById(int id);
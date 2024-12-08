package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.BudynekDao;
import org.example.mappers.BudynekMapper;
import org.example.mappers.BudynekMapperBuilder;
import org.example.model.Budynek;

import java.util.UUID;

public class BudynekCassandraRepository extends AbstractCassandraRepository {

    private final BudynekDao budynekDao;

    public BudynekCassandraRepository() {
        initSession();
        createTable();
        BudynekMapper budynekMapper = new BudynekMapperBuilder(getSession()).build();
        budynekDao = budynekMapper.budynekDao(CqlIdentifier.fromCql("buildings"), "budynki");
    }

//    private void createTable() {
//        // Tworzymy zapytanie do stworzenia tabeli "budynki"
//        SimpleStatement createBudynekTable =
//                SchemaBuilder.createTable(CqlIdentifier.fromCql("budynki"))
//                        .ifNotExists() // Jeśli tabela już istnieje, nie twórz jej ponownie
//                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
//                        .withColumn(CqlIdentifier.fromCql("nazwa"), DataTypes.TEXT)
////                        .withColumn(CqlIdentifier.fromCql("liczba_lokali"), DataTypes.INT)
//                        .withColumn(CqlIdentifier.fromCql("czynsz_calkowity"), DataTypes.DOUBLE)
//                        .build();
//
//        // Wykonanie zapytania w sesji Cassandra
//        getSession().execute(createBudynekTable);
//    }

    private void createTable() {
        // Tworzymy zapytanie do stworzenia tabeli "budynki"
        SimpleStatement createBudynekTable =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("budynki"))
                        .ifNotExists() // Jeśli tabela już istnieje, nie twórz jej ponownie
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)  // Klucz partycji
                        .withColumn(CqlIdentifier.fromCql("nazwa"), DataTypes.TEXT)  // Kolumna 'nazwa' jako TEXT
                        .withColumn(CqlIdentifier.fromCql("lokale"), DataTypes.TEXT)  // Dodanie kolumny 'lokale' jako TEXT
                        .withColumn(CqlIdentifier.fromCql("czynsz_calkowity"), DataTypes.DOUBLE)  // Kolumna 'czynsz_calkowity' jako DOUBLE
                        .build();

        // Wykonanie zapytania w sesji Cassandra
        getSession().execute(createBudynekTable);
    }



    public void addBudynek(Budynek budynek) {
        budynekDao.save(budynek);
    }

    public Budynek getBudynek(UUID id) {
        return budynekDao.findById(id);
    }

    public void updateBudynek(Budynek budynek) {
        budynekDao.save(budynek);
    }

    public boolean deleteBudynek(Budynek budynek) {
        return budynekDao.delete(budynek);
    }
}




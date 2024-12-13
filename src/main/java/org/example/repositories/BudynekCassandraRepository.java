package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.BudynekDao;
import org.example.mappers.BudynekMapper;
import org.example.mappers.BudynekMapperBuilder;
import org.example.model.Budynek;
import org.example.model.Lokal;

import java.util.UUID;

public class BudynekCassandraRepository extends AbstractCassandraRepository {

    private final BudynekDao budynekDao;

    public BudynekCassandraRepository() {
        initSession();
        createTable();
        BudynekMapper budynekMapper = new BudynekMapperBuilder(getSession()).build();
        budynekDao = budynekMapper.budynekDao(CqlIdentifier.fromCql("buildings"), "budynki");
    }

    // Tworzymy zapytanie do stworzenia tabeli "budynki"
    private void createTable() {
        SimpleStatement createBudynekTable =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("budynki"))
                        .ifNotExists() // Jeśli tabela już istnieje, nie twórz jej ponownie
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("nazwa"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("lokale"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("czynsz_calkowity"), DataTypes.DOUBLE)
                        .build();

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

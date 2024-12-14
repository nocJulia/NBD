package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.LokalDao;
import org.example.mappers.LokalMapper;
import org.example.mappers.LokalMapperBuilder;
import org.example.model.Lokal;

import java.util.UUID;

public class LokalCassandraRepository extends AbstractCassandraRepository {

    LokalDao lokalDao;

    public LokalCassandraRepository() {
        initSession();
        createTable();
        LokalMapper lokalMapper = new LokalMapperBuilder(getSession()).build();
        lokalDao = lokalMapper.lokalDao(CqlIdentifier.fromCql("buildings"), "lokale");
    }

    private void createTable() {
        SimpleStatement createLokaleTable =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("lokale"))
                        .ifNotExists() // Jeśli tabela już istnieje, nie twórz jej ponownie
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("stawka"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("koszty_dodatkowe"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("liczba_pokoi"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("powierzchnia_w_metrach"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("typ"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("budynek_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("czynsz_calkowity"), DataTypes.DOUBLE)
                        .build();

        getSession().execute(createLokaleTable);
    }


    public void addLokal(Lokal lokal) {
        lokalDao.save(lokal);
    }

    public Lokal getLokal(UUID id) {
        return lokalDao.findById(id);
    }

    public void updateLokal(Lokal lokal) {
        lokalDao.save(lokal);
    }

    public boolean deleteLokal(Lokal lokal) {
        return lokalDao.delete(lokal);
    }
}
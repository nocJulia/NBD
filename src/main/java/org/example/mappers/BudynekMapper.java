package org.example.mappers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import org.example.dao.BudynekDao;


@Mapper
public interface BudynekMapper {
    @DaoFactory
    BudynekDao budynekDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable String table);
}


package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;


public class AbstractCassandraRepository {
    private static CqlSession session;

    public static CqlSession getSession() {
        return session;
    }

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                //ponizsza linijke nalezy zakomentowac przy pierwszym uruchomieniu
                .withKeyspace(CqlIdentifier.fromCql("buildings"))
                .withAuthCredentials("cassandra", "cassandra")
                .build();

        initKeyspace();
    }

    private void initKeyspace() {
        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("buildings"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    public void dropKeyspace() {
        session.execute(
                SchemaBuilder.dropKeyspace(CqlIdentifier.fromCql("buildings"))
                        .ifExists()
                        .build()
        );
    }
}
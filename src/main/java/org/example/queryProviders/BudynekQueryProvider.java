package org.example.queryProviders;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import org.example.model.Budynek;
import org.example.model.Lokal;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class BudynekQueryProvider {
    private CqlSession session;

    public BudynekQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void save(Budynek budynek) {
        session.execute(
                QueryBuilder.insertInto("budynki")
                        .value("id", literal(budynek.getId()))
                        .value("nazwa", literal(budynek.getNazwa()))
                        .build()
        );

        for (Lokal lokal : budynek.getLokale()) {
            saveLokal(lokal, budynek.getId());
        }
    }

    public void update(Budynek budynek) {
        session.execute(
                QueryBuilder.update("budynki")
                        .setColumn("nazwa", literal(budynek.getNazwa()))
                        .where(Relation.column("id").isEqualTo(literal(budynek.getId())))
                        .build()
        );

        for (Lokal lokal : budynek.getLokale()) {
            updateLokal(lokal);
        }
    }

    public boolean delete(Budynek budynek) {
        session.execute(
                QueryBuilder.deleteFrom("budynki")
                        .where(Relation.column("id").isEqualTo(literal(budynek.getId())))
                        .build()
        );

        for (Lokal lokal : budynek.getLokale()) {
            deleteLokal(lokal);
        }

        return true;
    }

    public Budynek findById(UUID id) {
        Row row = session.execute(
                QueryBuilder.selectFrom("budynki")
                        .all()
                        .where(Relation.column("id").isEqualTo(literal(id)))
                        .build()
        ).one();

        if (row != null) {
            Budynek budynek = new Budynek(
                    row.getUuid("id"),
                    row.getString("nazwa")
            );

            List<Lokal> lokale = findLokaleForBudynek(id);
            budynek.setLokale(lokale);

            return budynek;
        }

        return null;
    }

    private void saveLokal(Lokal lokal, UUID budynekId) {
        session.execute(
                QueryBuilder.insertInto("lokale")
                        .value("id", literal(lokal.getId()))
                        .value("budynek_id", literal(budynekId))
                        .value("powierzchnia_w_metrach", literal(lokal.getPowierzchnia_w_metrach()))
                        .value("czynsz_calkowity", literal(lokal.getStawka()))
                        .build()
        );
    }

    private void updateLokal(Lokal lokal) {
        session.execute(
                QueryBuilder.update("lokale")
                        .setColumn("powierzchnia_w_metrach", literal(lokal.getPowierzchnia_w_metrach()))
                        .setColumn("czynsz_calkowity", literal(lokal.getStawka()))
                        .where(Relation.column("id").isEqualTo(literal(lokal.getId())))
                        .build()
        );
    }

    private void deleteLokal(Lokal lokal) {
        session.execute(
                QueryBuilder.deleteFrom("lokale")
                        .where(Relation.column("id").isEqualTo(literal(lokal.getId())))
                        .build()
        );
    }

    private List<Lokal> findLokaleForBudynek(UUID budynekId) {
        List<Lokal> lokale = new ArrayList<>();

        Row row = session.execute(
                QueryBuilder.selectFrom("lokale")
                        .all()
                        .where(Relation.column("budynek_id").isEqualTo(literal(budynekId)))
                        .build()
        ).one();

        while (row != null) {
            Lokal lokal = new Lokal(
                    row.getUuid("id"),
                    row.getDouble("powierzchnia_w_metrach"),
                    row.getDouble("stawka"),
                    row.getString("typ")
            ) {
                @Override
                public double czynsz() {
                    return getStawka() * getPowierzchnia_w_metrach();
                }
            };
            lokale.add(lokal);

            row = session.execute(
                    QueryBuilder.selectFrom("lokale")
                            .all()
                            .where(Relation.column("budynek_id").isEqualTo(literal(budynekId)))
                            .build()
            ).one();
        }

        return lokale;
    }
}

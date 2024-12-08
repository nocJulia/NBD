//package org.example.queryProviders;
//
//import com.datastax.oss.driver.api.core.CqlSession;
//import com.datastax.oss.driver.api.core.cql.Row;
//import com.datastax.oss.driver.api.mapper.MapperContext;
//import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
//import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
//import com.datastax.oss.driver.api.querybuilder.relation.Relation;
//import org.example.model.Budynek;
//import org.example.model.Lokal;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.ArrayList;
//
//import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
//
//public class BudynekQueryProvider {
//
//    private final CqlSession session;
//    private final EntityHelper<Budynek> budynekEntityHelper;
//
//    public BudynekQueryProvider(MapperContext ctx, EntityHelper<Budynek> budynekEntityHelper) {
//        this.session = ctx.getSession();
//        this.budynekEntityHelper = budynekEntityHelper;
//    }
//
//    // Save method for saving Budynek to Cassandra
//    public void save(Budynek budynek) {
//        // Prepare query to save Budynek
//        session.execute(
//                QueryBuilder.insertInto("budynki")
//                        .value("id", literal(budynek.getId()))
//                        .value("nazwa", literal(budynek.getNazwa()))
//                        .build()
//        );
//
//        // Save lokales associated with the Budynek
//        for (Lokal lokal : budynek.getLokale()) {
//            saveLokal(lokal, budynek.getId());
//        }
//    }
//
//    // Update method for updating a Budynek
//    public void update(Budynek budynek) {
//        // Prepare query to update Budynek
//        session.execute(
//                QueryBuilder.update("budynki")
//                        .setColumn("nazwa", literal(budynek.getNazwa()))
//                        .where(Relation.column("id").isEqualTo(literal(budynek.getId())))
//                        .build()
//        );
//
//        // Update lokales associated with the Budynek
//        for (Lokal lokal : budynek.getLokale()) {
//            updateLokal(lokal);
//        }
//    }
//
//    // Delete method for deleting a Budynek
//    public boolean delete(Budynek budynek) {
//        // Delete the Budynek
//        session.execute(
//                QueryBuilder.deleteFrom("budynki")
//                        .where(Relation.column("id").isEqualTo(literal(budynek.getId())))
//                        .build()
//        );
//
//        // Delete associated lokales
//        for (Lokal lokal : budynek.getLokale()) {
//            deleteLokal(lokal);
//        }
//
//        return true;
//    }
//
//    // Find method for finding a Budynek by id
//    public Budynek findById(UUID id) {
//        // Select query to find the Budynek
//        Row row = session.execute(
//                QueryBuilder.selectFrom("budynki")
//                        .all()
//                        .where(Relation.column("id").isEqualTo(literal(id)))
//                        .build()
//        ).one();
//
//        if (row != null) {
//            Budynek budynek = new Budynek(
//                    row.getUuid("id"),
//                    row.getString("nazwa")
//            );
//
//            // Fetch and associate Lokale with the Budynek
//            List<Lokal> lokale = findLokaleForBudynek(id);
//            budynek.setLokale(lokale);
//
//            return budynek;
//        }
//
//        return null;
//    }
//
//    // Helper method for saving Lokale (assumes there is a method for Lokale)
//    private void saveLokal(Lokal lokal, UUID budynekId) {
//        session.execute(
//                QueryBuilder.insertInto("lokale")
//                        .value("id", literal(lokal.getId()))
//                        .value("budynek_id", literal(budynekId))
//                        .value("powierzchnia", literal(lokal.getPowierzchnia_w_metrach()))
//                        .value("czynsz", literal(lokal.getStawka()))
//                        .build()
//        );
//    }
//
//    // Helper method for updating Lokale
//    private void updateLokal(Lokal lokal) {
//        session.execute(
//                QueryBuilder.update("lokale")
//                        .setColumn("powierzchnia", literal(lokal.getPowierzchnia_w_metrach()))
//                        .setColumn("czynsz", literal(lokal.getStawka()))
//                        .where(Relation.column("id").isEqualTo(literal(lokal.getId())))
//                        .build()
//        );
//    }
//
//    // Helper method for deleting Lokale
//    private void deleteLokal(Lokal lokal) {
//        session.execute(
//                QueryBuilder.deleteFrom("lokale")
//                        .where(Relation.column("id").isEqualTo(literal(lokal.getId())))
//                        .build()
//        );
//    }
//
//    // Method to fetch Lokale for a given Budynek
//    private List<Lokal> findLokaleForBudynek(UUID budynekId) {
//        List<Lokal> lokale = new ArrayList<>();
//
//        // Query to find all Lokale for a given Budynek
//        Row row = session.execute(
//                QueryBuilder.selectFrom("lokale")
//                        .all()
//                        .where(Relation.column("budynek_id").isEqualTo(literal(budynekId)))
//                        .build()
//        ).one();
//
//        while (row != null) {
//            Lokal lokal = new Lokal(
//                    row.getUuid("id"),
//                    row.getDouble("powierzchnia"),
//                    row.getDouble("stawka"),
//                    row.getString("typ")
//            ) {
//                @Override
//                public double czynsz() {
//                    return getStawka() *getPowierzchnia_w_metrach();
//                }
//            };
//            lokale.add(lokal);
//
//            row = session.execute(
//                    QueryBuilder.selectFrom("lokale")
//                            .all()
//                            .where(Relation.column("budynek_id").isEqualTo(literal(budynekId)))
//                            .build()
//            ).one();
//        }
//
//        return lokale;
//    }
//}


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

    // CqlSession is used directly for queries
    private CqlSession session;

    public BudynekQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();  // Initialize session from MapperContext
    }

    // Save method for saving Budynek to Cassandra
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

    // Update method for updating a Budynek
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

    // Delete method for deleting a Budynek
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

    // Find method for finding a Budynek by id
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
                        .value("powierzchnia", literal(lokal.getPowierzchnia_w_metrach()))
                        .value("czynsz", literal(lokal.getStawka()))
                        .build()
        );
    }

    private void updateLokal(Lokal lokal) {
        session.execute(
                QueryBuilder.update("lokale")
                        .setColumn("powierzchnia", literal(lokal.getPowierzchnia_w_metrach()))
                        .setColumn("czynsz", literal(lokal.getStawka()))
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
                    row.getDouble("powierzchnia"),
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

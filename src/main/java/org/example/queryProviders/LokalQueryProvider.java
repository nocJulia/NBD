//package org.example.queryProviders;
//
//import com.datastax.oss.driver.api.core.CqlIdentifier;
//import com.datastax.oss.driver.api.core.CqlSession;
//import com.datastax.oss.driver.api.core.cql.Row;
//import com.datastax.oss.driver.api.mapper.MapperContext;
//import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
//import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
//import com.datastax.oss.driver.api.querybuilder.relation.Relation;
//import com.datastax.oss.driver.api.querybuilder.select.Select;
//import org.example.model.Lokal;
//import org.example.model.Mieszkanie;
//import org.example.model.Biuro;
//import java.util.UUID;
//
//import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
//import static org.example.mappers.FromDBLokalMapper.*;
//
//public class LokalQueryProvider {
//    private final CqlSession session;
//    private final EntityHelper<Lokal> lokalEntityHelper;
//    private final EntityHelper<Mieszkanie> mieszkanieEntityHelper;
//    private final EntityHelper<Biuro> biuroEntityHelper;
//
//    public LokalQueryProvider(MapperContext ctx,
//                              EntityHelper<Lokal> lokalEntityHelper,
//                              EntityHelper<Mieszkanie> mieszkanieEntityHelper,
//                              EntityHelper<Biuro> biuroEntityHelper) {
//        this.session = ctx.getSession();
//        this.lokalEntityHelper = lokalEntityHelper;
//        this.mieszkanieEntityHelper = mieszkanieEntityHelper;
//        this.biuroEntityHelper = biuroEntityHelper;
//    }
//
//    // Save method for saving Mieszkanie or Biuro to Cassandra
//    public void save(Lokal lokal) {
//        session.execute(
//                switch (lokal.getTyp()) {
//                    case "mieszkanie" -> {
//                        Mieszkanie mieszkanie = (Mieszkanie) lokal;
//                        yield session.prepare(mieszkanieEntityHelper.insert().build())
//                                .bind()
//                                .setUuid("id", mieszkanie.getId())
//                                .setDouble("powierzchnia", mieszkanie.getPowierzchnia_w_metrach())
//                                .setDouble("stawka", mieszkanie.getStawka())
//                                .setString("typ_lokalu", mieszkanie.getTyp());
//                    }
//                    case "biuro" -> {
//                        Biuro biuro = (Biuro) lokal;
//                        yield session.prepare(biuroEntityHelper.insert().build())
//                                .bind()
//                                .setUuid("id", biuro.getId())
//                                .setDouble("powierzchnia", biuro.getPowierzchnia_w_metrach())
//                                .setDouble("stawka", biuro.getStawka())
//                                .setDouble("koszty_dodatkowe", biuro.dajKoszty())
//                                .setString("typ_lokalu", biuro.getTyp());
//                    }
//                    default -> throw new IllegalArgumentException("Unknown lokal type");
//                }
//        );
//    }
//
//    // Find method for retrieving Mieszkanie or Biuro by ID
//    public Lokal findById(UUID id) {
//        Select selectLokal = QueryBuilder.selectFrom(CqlIdentifier.fromCql("lokale"))
//                .all()
//                .where(Relation.column("id").isEqualTo(literal(id)));
//
//        Row row = session.execute(selectLokal.build()).one();
//        String typLokal = row.getString("typ_lokalu");
//
//        return switch (typLokal) {
//            case "mieszkanie" -> getMieszkanie(row);
//            case "biuro" -> getBiuro(row);
//            default -> throw new IllegalArgumentException("Unknown lokal type");
//        };
//    }
//
//    // Mapper for Mieszkanie
//    private Mieszkanie getMieszkanie(Row row) {
//        return new Mieszkanie(
//                row.getUuid("id"),
//                row.getDouble("powierzchnia"),
//                row.getDouble("stawka"),
//                row.getInt("liczba_pokoi")
//        );
//    }
//
//    // Mapper for Biuro
//    private Biuro getBiuro(Row row) {
//        return new Biuro(
//                row.getUuid("id"),
//                row.getDouble("powierzchnia"),
//                row.getDouble("stawka"),
//                row.getDouble("koszty_dodatkowe")
//        );
//    }
//}
//


package org.example.queryProviders;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.model.Biuro;

import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class LokalQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Mieszkanie> mieszkanieEntityHelper;
    private final EntityHelper<Biuro> biuroEntityHelper;

    // Konstruktor przyjmujący sesję i EntityHelper
    public LokalQueryProvider(MapperContext ctx,
                              EntityHelper<Mieszkanie> mieszkanieEntityHelper,
                              EntityHelper<Biuro> biuroEntityHelper) {
        this.session = ctx.getSession();
        this.mieszkanieEntityHelper = mieszkanieEntityHelper;
        this.biuroEntityHelper = biuroEntityHelper;
    }

    // Metoda zapisu Mieszkania lub Biura do bazy
    public Lokal save(Lokal lokal) {
        session.execute(
                switch (lokal.getTyp()) {
                    case "mieszkanie" -> {
                        Mieszkanie mieszkanie = (Mieszkanie) lokal;
                        yield session.prepare(mieszkanieEntityHelper.insert().build())
                                .bind()
                                .setUuid("id", mieszkanie.getId())
                                .setDouble("powierzchnia", mieszkanie.getPowierzchnia_w_metrach())
                                .setDouble("stawka", mieszkanie.getStawka())
                                .setString("typ_lokalu", mieszkanie.getTyp())
                                .setInt("liczba_pokoi", mieszkanie.getLiczbaPokoi());
                    }
                    case "biuro" -> {
                        Biuro biuro = (Biuro) lokal;
                        yield session.prepare(biuroEntityHelper.insert().build())
                                .bind()
                                .setUuid("id", biuro.getId())
                                .setDouble("powierzchnia", biuro.getPowierzchnia_w_metrach())
                                .setDouble("stawka", biuro.getStawka())
                                .setString("typ_lokalu", biuro.getTyp())
                                .setDouble("koszty_dodatkowe", biuro.dajKoszty());
                    }
                    default -> throw new IllegalArgumentException("Nieznany typ lokalu: " + lokal.getTyp());
                }
        );
        return lokal;
    }

    // Metoda wyszukiwania lokalu po ID
    public Lokal findById(UUID id) {
        Row row = session.execute(
                QueryBuilder.selectFrom(CqlIdentifier.fromCql("lokale"))
                        .all()
                        .where(Relation.column("id").isEqualTo(literal(id)))
                        .build()
        ).one();

        if (row == null) {
            return null; // Jeśli nie znaleziono w bazie
        }

        // Określenie typu lokalu i utworzenie odpowiedniego obiektu
        String typLokal = row.getString("typ_lokalu");

        return switch (typLokal) {
            case "mieszkanie" -> getMieszkanie(row);
            case "biuro" -> getBiuro(row);
            default -> throw new IllegalArgumentException("Nieznany typ lokalu: " + typLokal);
        };
    }

    // Mapper dla Mieszkania
    private Mieszkanie getMieszkanie(Row row) {
        return new Mieszkanie(
                row.getUuid("id"),
                row.getDouble("powierzchnia"),
                row.getDouble("stawka"),
                row.getInt("liczba_pokoi")
        );
    }

    // Mapper dla Biura
    private Biuro getBiuro(Row row) {
        return new Biuro(
                row.getUuid("id"),
                row.getDouble("powierzchnia"),
                row.getDouble("stawka"),
                row.getDouble("koszty_dodatkowe")
        );
    }
}




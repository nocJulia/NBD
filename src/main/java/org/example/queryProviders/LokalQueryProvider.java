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

    public LokalQueryProvider(MapperContext ctx,
                              EntityHelper<Mieszkanie> mieszkanieEntityHelper,
                              EntityHelper<Biuro> biuroEntityHelper) {
        this.session = ctx.getSession();
        this.mieszkanieEntityHelper = mieszkanieEntityHelper;
        this.biuroEntityHelper = biuroEntityHelper;
    }

    public Lokal save(Lokal lokal) {
        session.execute(
                switch (lokal.getTyp()) {
                    case "Mieszkanie" -> {
                        Mieszkanie mieszkanie = (Mieszkanie) lokal;
                        yield session.prepare(mieszkanieEntityHelper.insert().build())
                                .bind()
                                .setUuid("id", mieszkanie.getId())
                                .setDouble("powierzchnia_w_metrach", mieszkanie.getPowierzchnia_w_metrach())
                                .setDouble("stawka", mieszkanie.getStawka())
                                .setString("typ", mieszkanie.getTyp())
                                .setInt("liczba_pokoi", mieszkanie.getLiczbaPokoi());
                    }
                    case "Biuro" -> {
                        Biuro biuro = (Biuro) lokal;
                        yield session.prepare(biuroEntityHelper.insert().build())
                                .bind()
                                .setUuid("id", biuro.getId())
                                .setDouble("powierzchnia_w_metrach", biuro.getPowierzchnia_w_metrach())
                                .setDouble("stawka", biuro.getStawka())
                                .setString("typ", biuro.getTyp())
                                .setDouble("koszty_dodatkowe", biuro.dajKoszty());
                    }
                    default -> throw new IllegalArgumentException("Nieznany typ lokalu: " + lokal.getTyp());
                }
        );
        return lokal;
    }

    public Lokal findById(UUID id) {
        Row row = session.execute(
                QueryBuilder.selectFrom(CqlIdentifier.fromCql("lokale"))
                        .all()
                        .where(Relation.column("id").isEqualTo(literal(id)))
                        .build()
        ).one();

        if (row == null) {
            return null;
        }

        String typLokal = row.getString("typ");

        assert typLokal != null;
        return switch (typLokal) {
            case "Mieszkanie" -> getMieszkanie(row);
            case "Biuro" -> getBiuro(row);
            default -> throw new IllegalArgumentException("Nieznany typ lokalu: " + typLokal);
        };
    }

    private Mieszkanie getMieszkanie(Row row) {
        return new Mieszkanie(
                row.getUuid("id"),
                row.getDouble("powierzchnia_w_metrach"),
                row.getDouble("stawka"),
                row.getInt("liczba_pokoi")
        );
    }

    private Biuro getBiuro(Row row) {
        return new Biuro(
                row.getUuid("id"),
                row.getDouble("powierzchnia_w_metrach"),
                row.getDouble("stawka"),
                row.getDouble("koszty_dodatkowe")
        );
    }
}

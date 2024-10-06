CREATE TABLE IF NOT EXISTS budynek (
                                       id SERIAL PRIMARY KEY,
                                       nazwa VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS lokal (
                                     id SERIAL PRIMARY KEY,
                                     powierzchnia DOUBLE PRECISION NOT NULL,
                                     stawka DOUBLE PRECISION NOT NULL,
                                     typ_lokalu VARCHAR(255) NOT NULL,
    budynek_id BIGINT,
    koszty_dodatkowe DOUBLE PRECISION,
    CONSTRAINT fk_budynek FOREIGN KEY (budynek_id) REFERENCES budynek(id)
    );

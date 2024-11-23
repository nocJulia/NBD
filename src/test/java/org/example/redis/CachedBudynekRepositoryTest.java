package org.example.redis;

import org.bson.types.ObjectId;
import org.example.model.Budynek;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedBudynekRepository;
import org.example.mappers.LokalMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CachedBudynekRepositoryTest {

    private BudynekRepository mongoRepository; // prawdziwe repozytorium
    private JedisPooled jedis; // rzeczywiste połączenie z Redis
    private CachedBudynekRepository repository;
    private Budynek testBudynek; // Obiekt, który będzie testowany
    private Budynek testBudynek1;

    @BeforeEach
    public void setup() throws IOException {
        mongoRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(mongoRepository);
        mongoRepository.setLokalMapper(lokalMapper);

        // Inicjalizacja połączenia z Redis za pomocą RedisClient
        RedisClient redisClient = new RedisClient();
        redisClient.innitConnection(); // Inicjalizacja połączenia

        jedis = RedisClient.getPool(); // Pobranie instancji JedisPooled z RedisClient
        repository = new CachedBudynekRepository(mongoRepository, jedis);

        // Utworzenie testowego budynku
        testBudynek = new Budynek(new ObjectId(), "Testowy Budynek");
        mongoRepository.save(testBudynek); // Zapiszemy budynek do MongoDB
    }

    @AfterEach
    public void cleanup() {
        // Czyszczenie danych z MongoDB
        mongoRepository.clearCollection();

        // Czyszczenie cache w Redis
        repository.invalidateCache(testBudynek.getId()); // Usuwanie danych z Redis dla danego budynku
    }

    @Test
    void testSave() {
        // Wywołanie metody save
        testBudynek1 = new Budynek(new ObjectId(), "Testowy Budynek1");
        repository.save(testBudynek1);

        // Weryfikacja danych w MongoDB
        Budynek savedBudynek = mongoRepository.findById(testBudynek1.getId());
        assertNotNull(savedBudynek);
        assertEquals("Testowy Budynek1", savedBudynek.getNazwa());

        // Weryfikacja danych w Redis
        String redisKey = "budynek:" + testBudynek1.getId().toHexString();
        String redisValue = jedis.get(redisKey);
        assertNotNull(redisValue);
        assertEquals(
                "{\"id\":\"" + testBudynek1.getId().toHexString() + "\",\"name\":\"" + testBudynek1.getNazwa() + "\"}",
                redisValue
        );
    }

    @Test
    void testFindById_CacheMiss() {
        // Usunięcie danych z Redis, aby zasymulować cache miss
        jedis.del("budynek:" + testBudynek.getId().toHexString());

        // Wywołanie testowanej metody
        Budynek result = repository.findById(testBudynek.getId());

        // Weryfikacja, że dane zostały pobrane z MongoDB
        assertNotNull(result);
        assertEquals("Testowy Budynek", result.getNazwa());

        // Weryfikacja danych w Redis
        String redisValue = jedis.get("budynek:" + testBudynek.getId().toHexString());
        assertNotNull(redisValue);
        assertEquals(
                "{\"id\":\"" + testBudynek.getId().toHexString() + "\",\"name\":\"Testowy Budynek\"}",
                redisValue
        );
    }

}

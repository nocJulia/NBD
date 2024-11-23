package org.example.redis;

import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedBudynekRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CachedBudynekRepositoryTest {

    private BudynekRepository mongoRepository;
    private JedisPooled jedis;
    private CachedBudynekRepository repository;
    private Budynek testBudynek;

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
        Budynek testBudynek1 = new Budynek(new ObjectId(), "Testowy Budynek1");
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

    @Test
    void testFindById_RedisUnavailable() {
        // Mockowanie problemu z połączeniem do Redis (rzucenie wyjątku podczas wywołania 'get')
        JedisPooled mockedJedis = Mockito.spy(jedis); // Tworzymy mockowany obiekt na podstawie rzeczywistego
        doThrow(new JedisConnectionException("Redis connection failed"))
                .when(mockedJedis).get("budynek:" + testBudynek.getId().toHexString());

        // Zastąpienie połączenia w CachedBudynekRepository mockowanym Jedis
        CachedBudynekRepository repositoryWithMockedRedis =
                new CachedBudynekRepository(mongoRepository, mockedJedis);

        // Wywołanie metody findById
        Budynek result = repositoryWithMockedRedis.findById(testBudynek.getId());

        // Weryfikacja, że dane zostały pobrane z MongoDB
        assertNotNull(result);
        assertEquals(testBudynek.getNazwa(), result.getNazwa());

        // Weryfikacja, że dane z MongoDB zostały użyte
        Budynek mongoResult = mongoRepository.findById(testBudynek.getId());
        assertNotNull(mongoResult);
        assertEquals(testBudynek.getNazwa(), mongoResult.getNazwa());

        // Weryfikacja, że metoda 'setex' była wywołana pomimo problemów z Redis
        verify(mockedJedis, times(1)).setex(
                eq("budynek:" + testBudynek.getId().toHexString()),
                eq(3600L),
                anyString()
        );
    }


}
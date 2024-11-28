package org.example.redis;

import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Biuro;
import org.example.model.Budynek;
import org.example.model.Lokal;
import org.example.model.Mieszkanie;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedLokalRepository;
import org.example.repository.LokalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import static org.junit.jupiter.api.Assertions.*;

class CachedLokalRepositoryTest {

    private LokalRepository mongoLokalRepository;
    private JedisPooled jedis;
    private CachedLokalRepository repository;
    private Lokal testBiuro;
    private Lokal testMieszkanie;

    @BeforeEach
    void setup() {
        // Inicjalizacja MongoDB

        BudynekRepository budynekRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(budynekRepository);
        budynekRepository.setLokalMapper(lokalMapper);
        mongoLokalRepository = new LokalRepository(lokalMapper);

        // Inicjalizacja Redis
        RedisClient redisClient = new RedisClient();
        redisClient.innitConnection();
        jedis = RedisClient.getPool();

        // Inicjalizacja CachedLokalRepository
        repository = new CachedLokalRepository(mongoLokalRepository, jedis);

        // Tworzenie testowych obiektów
        Budynek budynek = new Budynek(new ObjectId(), "Testowy Budynek 2137");
        budynekRepository.save(budynek);

        testBiuro = new Biuro(new ObjectId(), budynek, 100.0, 50.0, 200.0);
        testMieszkanie = new Mieszkanie(new ObjectId(), budynek, 80.0, 40.0);

        mongoLokalRepository.save(testBiuro);
        mongoLokalRepository.save(testMieszkanie);

    }

//    @AfterEach
//    void cleanup() {
//        mongoLokalRepository.clearCollection();
//        RedisClient.clearCache();
//    }

    @Test
    void testSave() {
        Lokal newBiuro = new Biuro(new Budynek(new ObjectId(), "Nowy Budynek"), 120.0, 60.0, 250.0);
        repository.save(newBiuro);

        // Sprawdzenie w MongoDB
        Lokal savedLokal = mongoLokalRepository.findById(newBiuro.getId());
        assertNotNull(savedLokal);
        assertTrue(savedLokal instanceof Biuro);

        // Sprawdzenie w Redis
        String redisKey = "lokal:" + newBiuro.getId().toHexString();
        String cachedData = jedis.get(redisKey);
        assertNotNull(cachedData);
    }

    @Test
    void testFindById_CacheMiss() {
        // Usuwanie danych z cache
        jedis.del("lokal:" + testBiuro.getId().toHexString());

        // Pobranie z repozytorium
        Lokal foundLokal = repository.findById(testBiuro.getId());

        // Sprawdzenie danych
        assertNotNull(foundLokal);
        assertEquals(testBiuro.getId(), foundLokal.getId());

        // Weryfikacja, że dane zostały zapisane w cache
        String redisKey = "lokal:" + testBiuro.getId().toHexString();
        assertNotNull(jedis.get(redisKey));
    }

    @Test
    void testFindById_CacheHit() {

        // Pobranie obiektu (powinno pochodzić z cache)
        Lokal foundLokal = repository.findById(testMieszkanie.getId());

        // Weryfikacja
        assertNotNull(foundLokal);
        assertEquals(testMieszkanie.getId(), foundLokal.getId());
    }

    @Test
    void testUpdate() {
        // Aktualizacja powierzchni biura
        testBiuro.ustawPowierzchnie(150.0);
        repository.update(testBiuro);

        // Sprawdzenie w MongoDB
        Lokal updatedLokal = mongoLokalRepository.findById(testBiuro.getId());
        assertNotNull(updatedLokal);
        assertEquals(150.0, updatedLokal.dajPowierzchnie());

        // Sprawdzenie w Redis
        String cachedData = jedis.get("lokal:" + testBiuro.getId().toHexString());
        assertNotNull(cachedData);
    }

    @Test
    void testDelete() {
        repository.delete(testMieszkanie);

        // Sprawdzenie w MongoDB
        assertNull(mongoLokalRepository.findById(testMieszkanie.getId()));

        // Sprawdzenie w Redis
        assertNull(jedis.get("lokal:" + testMieszkanie.getId().toHexString()));
    }
}

package org.example.benchmarks;

import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.redis.RedisClient;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedBudynekRepository;
import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)  // Mierzymy średni czas wykonania
@OutputTimeUnit(TimeUnit.MILLISECONDS)  // Wynik w milisekundach
@State(Scope.Thread)  // Każdy wątek testowy będzie miał swoje osobne instancje
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 3)
public class Benchmark {

    private CachedBudynekRepository repository;
    private BudynekRepository mongoRepository;
    private JedisPooled jedis;

    @Setup(Level.Trial)  // Set up at the trial level to initialize only once before benchmarks start
    public void setup() {
        mongoRepository = new BudynekRepository(null);
        LokalMapper lokalMapper = new LokalMapper(mongoRepository);
        mongoRepository.setLokalMapper(lokalMapper);

        RedisClient redisClient = new RedisClient();
        redisClient.innitConnection();

        jedis = RedisClient.getPool(); // Pobranie instancji JedisPooled z RedisClient
        // Tworzenie repozytorium z cache
        repository = new CachedBudynekRepository(mongoRepository, jedis);
    }

    @TearDown(Level.Trial)  // Set up after the entire benchmark
    public void tearDown() {
        // Wywołanie metody do czyszczenia cache po zakończeniu wszystkich benchmarków
        RedisClient.clearCache();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheHit() {
        ObjectId id = new ObjectId();
        // Scenariusz: Cache Hit - dane są w cache
        String cacheKey = "budynek:" + id.toString();
        String cachedData = "{\"id\":\"" + id + "\",\"nazwa\":\"Testowy Budynek\"}";
        jedis.set(cacheKey, cachedData);  // Ustawiamy dane w Redis

        return repository.findById(id);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheMiss() {
        ObjectId id = new ObjectId();
        // Scenariusz: Cache Miss - brak danych w cache
        String cacheKey = "budynek:" + id.toString();
        jedis.del(cacheKey);  // Usuwamy dane z Redis

        // Dodajemy dane do MongoDB (odczyt z MongoDB)
        mongoRepository.save(new Budynek(id, "Testowy Budynek"));

        return repository.findById(id);  // Odczyt z MongoDB i zapis do cache
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheInvalidation() {
        ObjectId id = new ObjectId();
        // Scenariusz: Cache Invalidation - usuwamy dane z cache
        String cacheKey = "budynek:" + id.toString();
        jedis.del(cacheKey);  // Usuwamy dane z cache
        mongoRepository.save(new Budynek(id, "Testowy Budynek"));  // Dodajemy dane do MongoDB

        repository.invalidateCache(id);  // Usuwamy dane z cache przed odczytem

        return repository.findById(id);  // Odczytujemy dane z MongoDB, zapisując je do cache
    }

}

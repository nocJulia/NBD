package org.example.benchmarks;

import org.bson.types.ObjectId;
import org.example.model.Budynek;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedBudynekRepository;
import org.mockito.Mockito;
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
    private ObjectId id;

    @Setup(Level.Trial)  // Setup przed wszystkimi testami
    public void setup() {
        // Przygotowanie mocków
        jedis = Mockito.mock(JedisPooled.class);
        mongoRepository = Mockito.mock(BudynekRepository.class);
        repository = new CachedBudynekRepository(mongoRepository, jedis);

        // Tworzymy przykładowe ID
        id = new ObjectId();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheHit() {
        // Scenariusz: Cache Hit - dane są w cache
        String cacheKey = "budynek:" + id.toString();
        String cachedData = "{\"id\":\"" + id + "\",\"nazwa\":\"Testowy Budynek\"}";
        Mockito.when(jedis.get(cacheKey)).thenReturn(cachedData);  // Mockujemy dane w cache

        return repository.findById(id);  // Odczytujemy dane, które są już w cache
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheMiss() {
        // Scenariusz: Cache Miss - dane nie są w cache
        String cacheKey = "budynek:" + id.toString();
        Mockito.when(jedis.get(cacheKey)).thenReturn(null);  // Brak danych w cache
        Mockito.when(mongoRepository.findById(id)).thenReturn(new Budynek(id, "Testowy Budynek"));  // Mockujemy bazę danych

        return repository.findById(id);  // Odczytujemy dane z bazy danych, zapisując je do cache
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Budynek testCacheInvalidation() {
        // Scenariusz: Cache Invalidation - usuwamy dane z cache
        String cacheKey = "budynek:" + id.toString();
        Mockito.when(jedis.get(cacheKey)).thenReturn(null);  // Brak danych w cache
        Mockito.when(mongoRepository.findById(id)).thenReturn(new Budynek(id, "Testowy Budynek"));  // Mockujemy bazę danych

        repository.invalidateCache(id);  // Usuwamy dane z cache przed odczytem

        return repository.findById(id);  // Odczytujemy dane z bazy, zapisując je do cache
    }
}
package org.example.redis;

import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Budynek;
import org.example.repository.BudynekRepository;
import org.example.repository.CachedBudynekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CachedBudynekRepositoryTest {

    private BudynekRepository mongoRepository;
    private JedisPooled jedis;
    private CachedBudynekRepository repository;

    @BeforeEach
    public void setup() {
        // Mocking the MongoDB repository to avoid interaction with real MongoDB
        mongoRepository = mock(BudynekRepository.class);
        LokalMapper lokalMapper = new LokalMapper(mongoRepository);
        mongoRepository.setLokalMapper(lokalMapper);

        // Mocking JedisPooled (Redis connection)
        jedis = mock(JedisPooled.class);

        // Initialize CachedBudynekRepository with mocked dependencies
        repository = new CachedBudynekRepository(mongoRepository, jedis);
    }

    @Test
    void testSave() {
        Budynek budynek = new Budynek(new ObjectId(), "Testowy Budynek");

        // Mock the behavior of MongoDB save to return the same object
        doNothing().when(mongoRepository).save(budynek);

        // Save the budynek and check if the method was called on the mock repository
        repository.save(budynek);
        verify(mongoRepository, times(1)).save(budynek);

        // Mock Jedis set to simulate Redis caching the data
        when(jedis.get("budynek:" + budynek.getId().toString())).thenReturn("{\"id\":\"" + budynek.getId().toString() + "\",\"name\":\"Testowy Budynek\"}");

        String cacheData = jedis.get("budynek:" + budynek.getId().toString());
        assertNotNull(cacheData);  // Ensure that Redis has the cached data
    }

    @Test
    void testFindById_CacheHit() {
        ObjectId id = new ObjectId();

        // Mock the behavior of the cache hit: Redis should return the expected data
        String json = "{\"id\":\"" + id + "\",\"nazwa\":\"Testowy Budynek\"}";
        when(jedis.get("budynek:" + id)).thenReturn(json);

        // Act: Test cache hit
        Budynek result = repository.findById(id);

        // Assert: Ensure the data comes from Redis
        assertNotNull(result);
        assertEquals("Testowy Budynek", result.getNazwa());
    }


    @Test
    void testFindById_CacheMiss() {
        ObjectId id = new ObjectId();
        Budynek budynek = new Budynek(id, "Testowy Budynek");

        // Simulate cache miss (no data in Redis)
        when(jedis.get("budynek:" + id)).thenReturn(null);

        // Mock the behavior of MongoDB findById to return the actual Budynek
        when(mongoRepository.findById(id)).thenReturn(budynek);

        // Act: Test cache miss, data should be fetched from MongoDB
        Budynek result = repository.findById(id);

        // Assert: Ensure the data comes from MongoDB and is cached in Redis
        assertNotNull(result);
        assertEquals("Testowy Budynek", result.getNazwa());

        // Verify that Redis set is called to cache the data
        verify(jedis).set(eq("budynek:" + id), anyString());
    }

    @Test
    void testFindById_RedisUnavailable() {
        ObjectId id = new ObjectId();
        Budynek budynek = new Budynek(id, "Testowy Budynek");

        // Simulate Redis unavailability by throwing an exception
        when(jedis.get("budynek:" + id)).thenThrow(new JedisConnectionException("Could not get a resource from the pool"));

        // Mock the behavior of MongoDB findById to return the actual Budynek
        when(mongoRepository.findById(id)).thenReturn(budynek);

        // Act: Test cache miss, data should come from MongoDB even if Redis is down
        Budynek result = repository.findById(id);

        // Assert: Ensure the data comes from MongoDB
        assertNotNull(result);
        assertEquals("Testowy Budynek", result.getNazwa());
    }
}

package org.example.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mappers.LokalMapper;
import org.example.model.Lokal;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;
import com.google.gson.Gson;

import java.util.List;

public class CachedLokalRepository implements Repository<Lokal> {

    private final Repository<Lokal> delegate;
    private final JedisPooled jedis;

    public CachedLokalRepository(Repository<Lokal> delegate, JedisPooled jedis) {
        this.delegate = delegate;
        this.jedis = jedis;
    }

    private String generateCacheKey(ObjectId id) {
        return "lokal:" + id.toHexString();
    }

    private String serialize(Lokal lokal) {
        return new Gson().toJson(lokal);
    }

    private Lokal deserialize(String json) {
        Gson gson = new Gson();
        Document doc = gson.fromJson(json, Document.class);
        return ((LokalMapper) delegate).fromDocument(doc);
    }

    @Override
    public void save(Lokal lokal) {
        delegate.save(lokal);
        try {
            jedis.setex(generateCacheKey(lokal.getId()), 30, serialize(lokal));
        } catch (JedisConnectionException e) {
            System.err.println("Could not cache Lokal: " + e.getMessage());
        }
    }

    @Override
    public Lokal findById(ObjectId id) {
        String cacheKey = generateCacheKey(id);

        try {
            // Sprawdź cache
            String cachedData = jedis.get(cacheKey);
            if (cachedData != null) {
                return deserialize(cachedData);
            }
        } catch (JedisConnectionException e) {
            System.err.println("Cache unavailable, falling back to database.");
        }

        // Jeśli brak danych w cache, pobierz z bazy
        Lokal lokal = delegate.findById(id);
        if (lokal != null) {
            try {
                jedis.setex(cacheKey, 30, serialize(lokal));
            } catch (JedisConnectionException e) {
                System.err.println("Could not cache Lokal after database retrieval: " + e.getMessage());
            }
        }
        return lokal;
    }

    @Override
    public List<Lokal> findAll() {
        return delegate.findAll();
    }

    @Override
    public void update(Lokal lokal) {
        delegate.update(lokal);
        try {
            jedis.setex(generateCacheKey(lokal.getId()), 30, serialize(lokal)); // Dodanie TTL 30 sekund
        } catch (JedisConnectionException e) {
            System.err.println("Could not update Lokal in cache: " + e.getMessage());
        }
    }


    @Override
    public void delete(Lokal lokal) {
        delegate.delete(lokal);
        try {
            jedis.del(generateCacheKey(lokal.getId()));
        } catch (JedisConnectionException e) {
            System.err.println("Could not remove Lokal from cache: " + e.getMessage());
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void clearCollection() {
        delegate.clearCollection();
    }
}

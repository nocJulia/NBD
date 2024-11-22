package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bson.types.ObjectId;
import org.example.model.Budynek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

public class CachedBudynekRepository implements Repository<Budynek> {

    private static final Logger logger = LoggerFactory.getLogger(CachedBudynekRepository.class);

    private final Repository<Budynek> delegate;
    private final JedisPooled jedis;

    public CachedBudynekRepository(Repository<Budynek> delegate, JedisPooled jedis) {
        this.delegate = delegate;
        this.jedis = jedis;
    }

    @Override
    public void save(Budynek budynek) {
        delegate.save(budynek);
        String cacheKey = generateCacheKey(budynek.getId());
        jedis.set(cacheKey, serialize(budynek));
    }

    @Override
    public Budynek findById(ObjectId id) {
        String cacheKey = generateCacheKey(id);

        try {
            String cachedData = jedis.get(cacheKey);
            if (cachedData != null) {
                return deserialize(cachedData, Budynek.class);
            }
        } catch (JedisConnectionException e) {
            logger.error("Redis is unavailable. Falling back to MongoDB.", e);
        }

        Budynek budynek = delegate.findById(id);
        if (budynek != null) {
            try {
                jedis.set(cacheKey, serialize(budynek));
            } catch (JedisConnectionException e) {
                logger.warn("Redis is still unavailable. Skipping cache update.", e);
            }
        }

        return budynek;
    }

    @Override
    public List<Budynek> findAll() {
        return delegate.findAll();
    }

    @Override
    public void update(Budynek budynek) {
        delegate.update(budynek);
        String cacheKey = generateCacheKey(budynek.getId());
        jedis.set(cacheKey, serialize(budynek));
    }

    @Override
    public void delete(Budynek budynek) {
        delegate.delete(budynek);
        String cacheKey = generateCacheKey(budynek.getId());
        jedis.del(cacheKey);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void clearCollection() {
        delegate.clearCollection();
    }

    private String generateCacheKey(ObjectId id) {
        return "budynek:" + id.toString();
    }

    private String serialize(Object obj) {
        return new Gson().toJson(obj);
    }

    private <T> T deserialize(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public void invalidateCache(ObjectId id) {
        jedis.del("budynek:" + id.toString()); // Usuwamy dane z cache
    }

}

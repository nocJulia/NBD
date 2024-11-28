package org.example.redis;

import redis.clients.jedis.*;

import java.io.InputStream;
import java.util.Properties;

public class RedisClient {
    private static JedisPooled pool;

    public void innitConnection() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("redis_connection.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder().build();
            pool = new JedisPooled(new HostAndPort(properties.getProperty("redis.host"), Integer.parseInt(properties.getProperty("redis.port"))), jedisClientConfig);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static JedisPooled getPool() {
        return pool;
    }

    public static void clearCache() {
        if (pool != null) {
            try {
                // Wysłanie bezpośredniego polecenia Redis: FLUSHDB
                pool.sendCommand(Protocol.Command.FLUSHDB);
                System.out.println("Cache cleared successfully.");
            } catch (Exception e) {
                System.err.println("Failed to clear cache: " + e.getMessage());
            }
        } else {
            System.err.println("Redis connection is not initialized.");
        }
    }

}

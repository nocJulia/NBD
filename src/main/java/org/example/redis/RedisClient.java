package org.example.redis;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisClient {
    private static JedisPooled pool;

    void innitConnection() throws IOException {
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
}

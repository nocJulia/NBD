package org.example.redis;

import lombok.Getter;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RedisClient {
    @Getter
    private static JedisPooled pool;
    private void innitConnection() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        }

        String host = properties.getProperty("redis.host");
        int port = Integer.parseInt(properties.getProperty("redis.port"));

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        pool = new JedisPooled(host, port, clientConfig.isSsl());
    }

}

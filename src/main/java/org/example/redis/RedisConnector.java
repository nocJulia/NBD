package org.example.redis;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.InputStream;
import java.util.Properties;

public class RedisConnector {
    private JedisPooled jedis;
    private void innitConnection(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("redis_connection.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder().build();
            jedis = new JedisPooled(new HostAndPort(properties.getProperty("redis.host"),Integer.parseInt(properties.getProperty("redis.port"))),jedisClientConfig);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public RedisConnector() {
        innitConnection();
    }
    public JedisPooled getJedis(){
        return jedis;
    }
}

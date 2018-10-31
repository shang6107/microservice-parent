package com.microservice.redpackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 08:26:04
 */
@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate redisTemplate(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName("localhost");
        factory.setPort(6379);
        factory.afterPropertiesSet();
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }
}

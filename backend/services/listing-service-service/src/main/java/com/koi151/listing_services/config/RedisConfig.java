package com.koi151.listing_services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${REDIS_PORT}")
    private int redisPort;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Bean
    public LettuceConnectionFactory redisConnection() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        if ("docker".equalsIgnoreCase(activeProfile)) {
            configuration.setHostName("ms-redis");
            configuration.setPort(redisPort);
        } else {
            configuration.setHostName("localhost");
            configuration.setPort(6380);
        }

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnection) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnection);
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                ));
    }
}

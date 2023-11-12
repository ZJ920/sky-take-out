package com.sky.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sky.json.JacksonObjectMapper;
import com.sky.utils.LocalDateTimeSerializer;
import io.lettuce.core.dynamic.RedisCommandFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;

@Configuration
@Slf4j
public class RedisConfiguration extends CachingConfigurerSupport {

    /**
     * 当前配置类不是必须的，因为 Spring Boot 框架会自动装配 RedisTemplate 对象，但是默认的key序列化器为
     * JdkSerializationRedisSerializer，导致我们存到Redis中后的数据和原始数据有差别，故设置为
     * StringRedisSerializer序列化器。
     *
     * @param redisConnectionFactory 连接工厂对象
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redis模板对象...");
        log.info("RedisConnectionFactory连接工厂对象: {}", redisConnectionFactory.toString());
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //设置redis value的序列化器，实现自定义对象序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(new JacksonObjectMapper());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

    /**
     * <p>RedisConfig 此类用于：Redis相关配置，用于解决存入Redis中值乱码问题 </p>
     * <p>@author：hujm</p>
     * <p>@date：2022年08月18日 18:04</p>
     * <p>@remark：</p>
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties,ObjectMapper objectMapper) {

        /*
        看起来你的 Setmeal 类中包含了 LocalDateTime 类型的 createTime 和 updateTime 属性，而在你之前提供的 Redis 配置中使用了 GenericJackson2JsonRedisSerializer 来序列化值。

        这可能会导致在将 Setmeal 对象序列化为 JSON 格式时出现问题，因为 GenericJackson2JsonRedisSerializer 可能无法正确处理 LocalDateTime 类型的属性。

        针对这个问题，你可以考虑使用自定义的 Redis 序列化器来处理 LocalDateTime 类型。你可以创建一个实现了 RedisSerializer 接口的类，专门用于对 LocalDateTime 类型进行序列化和反序列化。
        通过使用自定义的 Redis 序列化器来处理 LocalDateTime 类型，你应该可以解决无法正确序列化 LocalDateTime 类型属性的问题。希望这能够帮助你解决这个错误。
         */
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        RedisSerializer<LocalDateTime> localDateTimeSerializer = new LocalDateTimeSerializer();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(localDateTimeSerializer));

        config = config.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(
                                new GenericJackson2JsonRedisSerializer(objectMapper)
                        ));


        // 序列化值
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 设置TTL
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        // 设置键前缀
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        // 是否缓存空值
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        // 是否使用键前缀
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 注册 JavaTime 模块以支持 LocalDateTime
        return objectMapper;
    }
}

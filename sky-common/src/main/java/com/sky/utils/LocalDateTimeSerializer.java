package com.sky.utils;

import org.apache.commons.lang.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer implements RedisSerializer<LocalDateTime> {

    @Override
    public byte[] serialize(LocalDateTime localDateTime) throws SerializationException {
        if (localDateTime == null) {
            return new byte[0];
        }
        return localDateTime.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public LocalDateTime deserialize(byte[] bytes) throws SerializationException {
        if (bytes.length == 0) {
            return null;
        }
        return LocalDateTime.parse(new String(bytes, StandardCharsets.UTF_8));
    }
}

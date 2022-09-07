package com.freedom.mojito.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.freedom.mojito.common.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description:
 * <p>CreateTime: 2022-09-04 下午 9:17</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Configuration
public class RedisConfig {

    /**
     * 自定义redisTemplate（修改序列化方式）
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用StringRedisSerializer来序列化和反序列化Redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化Redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 配置对象映射器
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();
        // 指定要序列化的域，field，get和set，以及修饰符范围。ANY指包括private和public修饰符范围
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入类型，类的信息也将添加到json中，这样才可以根据类名反序列化。
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        // 将对象映射器添加到序列化器中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 配置key，value，hashKey，hashValue的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}

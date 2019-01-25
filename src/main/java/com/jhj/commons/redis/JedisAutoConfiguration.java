package com.jhj.commons.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;


/**
 * Jedis配置
 * 
 * @author zhangbo
 */
@Configuration
@EnableConfigurationProperties(value = JedisProperties.class)
@ConditionalOnProperty(prefix = "jedis", value = "enabled", havingValue = "true")
public class JedisAutoConfiguration {
	
	@Autowired
	private JedisProperties jedisProperties;

	@Bean
	public JedisPool jedisPool(JedisPoolConfig config) {
		return new JedisPool(config, 
				jedisProperties.getHost(), 
				jedisProperties.getPort(), 
				Protocol.DEFAULT_TIMEOUT,
				jedisProperties.getPassword());
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(jedisProperties.getPool().getMaxActive());
		config.setMaxIdle(jedisProperties.getPool().getMaxIdle());
		config.setMaxWaitMillis(jedisProperties.getPool().getMaxWait());
		return config;
	}
	
	@Bean
	public RedisService redisService() {
		RedisServiceImpl redisService = new RedisServiceImpl();
		redisService.setJedisPool(jedisPool(jedisPoolConfig()));
		redisService.setRedisTemplate(redisTemplate(redisConnectionFactory, stringRedisSerializer()));
		return redisService;
	}


	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	@ConditionalOnMissingBean(value = StringRedisSerializer.class)
	public StringRedisSerializer stringRedisSerializer() {
		return new StringRedisSerializer();
	}

	//使用StringRedisSerializer来代替默认的JdkSerializationRedisSerializer.这样才能使用REDIS中字符串类型提供的原生API
	@Bean
	@ConditionalOnMissingBean(value = RedisTemplate.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, StringRedisSerializer stringRedisSerializer) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(stringRedisSerializer);

		return template;
	}

}

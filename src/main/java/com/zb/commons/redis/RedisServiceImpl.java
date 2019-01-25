package com.zb.commons.redis;

import com.zb.commons.validate.CommonValidateUtil;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Setter
public class RedisServiceImpl implements RedisService {

    /**
     * redis template, based on spring
     */
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * direct based on jedis
     */
    private JedisPool jedisPool;

    /**
     * 金惠家REDIS对象统一前缀
     */
    private static final String JHJ_REDIS_KEY_PREFIX = "jhj_";

    @Override
    public Set<String> getKeys(String keyPattern) {
        if (CommonValidateUtil.isEmpty(keyPattern)) {
            return null;
        }
        return redisTemplate.keys(JHJ_REDIS_KEY_PREFIX + keyPattern);
    }

    @Override
    public void put(String key, String value, int expire) {
        redisTemplate.opsForValue().set(JHJ_REDIS_KEY_PREFIX + key, value);
        redisTemplate.expire(JHJ_REDIS_KEY_PREFIX + key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void persist(String key, String value) {
        redisTemplate.opsForValue().set(JHJ_REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(JHJ_REDIS_KEY_PREFIX + key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(JHJ_REDIS_KEY_PREFIX + key);
    }
    
    @Override
    public boolean keyExists(String key) {
        return redisTemplate.hasKey(JHJ_REDIS_KEY_PREFIX + key);
    }
    
    @Override
    public long stringIncr(String key, long incrNum) {
        return redisTemplate.opsForValue().increment(JHJ_REDIS_KEY_PREFIX + key, incrNum);
    }

    @Override
    public void hashPut(String key, String field, String value, int expire) {
        redisTemplate.opsForHash().put(JHJ_REDIS_KEY_PREFIX + key, field, value);
        redisTemplate.expire(JHJ_REDIS_KEY_PREFIX + key, expire, TimeUnit.SECONDS);
    }

    @Override
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(JHJ_REDIS_KEY_PREFIX + key, field);
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll(JHJ_REDIS_KEY_PREFIX + key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void hashPutAll(String key, Map<String, String> values) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hmset(JHJ_REDIS_KEY_PREFIX + key, values);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long createOrUpdateListByLeftPush(String key, String value, int expire) {
        long len = redisTemplate.opsForList().leftPush(JHJ_REDIS_KEY_PREFIX + key, value);
        if (expire > 0)
            redisTemplate.expire(JHJ_REDIS_KEY_PREFIX + key, expire, TimeUnit.SECONDS);
        return len;
    }

    @Override
    public List<Object> getList(String key, long startIndex, long endIndex) {
        return redisTemplate.opsForList().range(JHJ_REDIS_KEY_PREFIX + key, startIndex, endIndex);
    }
    
    @Override
    public long getListLength(String key) {
        return redisTemplate.opsForList().size(JHJ_REDIS_KEY_PREFIX + key);
    }

    @Override
    public void listElementPut(String key, long index, String value) {
        redisTemplate.opsForList().set(JHJ_REDIS_KEY_PREFIX + key, index, value);
    }

    @Override
	public Object listRightPop(String key) {
		return redisTemplate.opsForList().rightPop(JHJ_REDIS_KEY_PREFIX + key);
	}
    
    @Override
	public Object listRightPopAndLeftPush(String sourceKey, String destinationKey) {
    	return redisTemplate.opsForList().rightPopAndLeftPush(JHJ_REDIS_KEY_PREFIX + sourceKey, JHJ_REDIS_KEY_PREFIX + destinationKey);
	}
    
    @Override
    public boolean setTTL(String key, int expire) {
        return redisTemplate.expire(JHJ_REDIS_KEY_PREFIX + key, expire, TimeUnit.SECONDS);
    }

    @Override
    public Long getTTL(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(JHJ_REDIS_KEY_PREFIX + key, timeUnit);
    }

    /**
     * 尝试获得锁
     * @param lockName       锁名
     * @param acquireTimeout 单位为秒
     * @param keyTimeout     单位为秒
     * @return
     */
    @Override
	public String tryLock(String lockName, long acquireTimeout, long keyTimeout) {
		
		Jedis jedisConn = null;
		try {
			// 获取连接
			jedisConn = jedisPool.getResource();
			// 随机生成一个标识符
			String identifier = UUID.randomUUID().toString();
			// 锁名
			String lockKey = "distributed_lock_" + lockName;
			
			if (keyTimeout < 1) {
				// 锁持有的最长时间,默认1秒
				keyTimeout = 1;
			}
			
			// 尝试获取锁的超时时间,超过这个时间则放弃获取锁的动作
			long acquireThreshold = System.currentTimeMillis() + (acquireTimeout * 1000);
			while (System.currentTimeMillis() < acquireThreshold) {
				String result = jedisConn.set(lockKey, identifier, "NX", "EX", keyTimeout);
				// 获取锁成功
		        if ("OK".equals(result)) {
		            return identifier;
		        }
	
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		
		}  catch (JedisException e) {
            throw new RuntimeException(e);
        } finally {
            if (jedisConn != null) {
            	jedisConn.close();
            }
        }
		
		return null;
	}
	
	private static final Long RELEASE_SUCCESS = 1L;
	
    @Override
	public boolean releaseLock(String lockName, String identifier) {
		
		Jedis jedisConn = null;
		
        String lockKey = "distributed_lock_" + lockName;
        boolean flag = false;
        try {
        	jedisConn = jedisPool.getResource();
        	
        	String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedisConn.eval(script, Collections.singletonList(lockKey), Collections.singletonList(identifier));
            
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } catch (JedisException e) {
        	throw new RuntimeException(e);
        } finally {
            if (jedisConn != null) {
            	jedisConn.close();
            }
        }
        return flag;
    }

}

package com.zb.commons.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于jedis封装的Redis Service Interface
 * 
 * <p>
 * 
 * 有关Redis的配置,请参考com.jhj.chexian.api.cfg.RedisConfig
 * 
 * @author Zhang Bo
 */
public interface RedisService {

    /**
     * 获取符合规则的键名集合
     * @param keyPattern
     * @return
     */
    public Set<String> getKeys(String keyPattern);

    /**
     * 缓存.基于 Redis String 类型
     * @param key
     * @param value
     * @param expire
     */
    public void put(String key, String value, int expire);

    /**
     * 存储.基于 Redis String 类型
     * @param key
     * @param value
     */
    public void persist(String key, String value);

    /**
     * 根据字符串类型键的键名获取匹配的键值
     * @param key
     * @return
     */
    public Object get(String key);

    /**
     * 根据键名删除匹配的键
     *
     * @param key
     */
    public void delete(String key);

    /**
     * 判断指定的键是否存在
     * @param key
     * @return
     */
    public boolean keyExists(String key);

    /**
     * 增加指定字符串类型键的键值
     * <p>
     * 如果键值非数值,执行此操作时将抛出异常
     *
     * @param key
     * @param incrNum
     * @return
     */
    public long stringIncr(String key, long incrNum);

    /**
     * 缓存.基于 Redis Hash 类型
     * @param key
     * @param field
     * @param value
     * @param expire
     */
    public void hashPut(String key, String field, String value, int expire);

    /**
     * 根据散列类型键的键名和字段名获取匹配的键值
     * @param key
     * @param field
     * @return
     */
    public Object hashGet(String key, String field);

    /**
     * 根据散列类型键的键名获取该散列的全部数据
     * @param key
     * @return
     */
    public Map<String, String> hashGetAll(String key);
    
    /**
     * 根据散列类型键的键名获取该散列的全部数据
     * @param key
     * @param values 
     * @return
     */
    public void hashPutAll(String key, Map<String, String> values);

    /**
     * 创建或更新指定列表类型键的键名对应的 Redis List(从列表左边插入元素)
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public long createOrUpdateListByLeftPush(String key, String value, int expire);
    
    /**
     * 根据列表类型键的键名获取匹配的 Redis List
     * @param key
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<Object> getList(String key, long startIndex, long endIndex);

    /**
     * 根据列表类型键的键名获取匹配的 Redis List Size
     * <p>
     * 如果列表类型键不存在时,将返回零
     *
     * @param key
     * @return
     */
    public long getListLength(String key);

    /**
     * 将元素插入到 Redis List 的指定位置
     * @param key
     * @param index
     * @param value
     */
    public void listElementPut(String key, long index, String value);
    
    /**
     * 从 Redis List 右端弹出元素
     * @param key
     * @return
     */
    public Object listRightPop(String key);
    
    public Object listRightPopAndLeftPush(String sourceKey, String destinationKey);

    /**
     * 为指定的键设置过期日期
     *
     * @param key
     * @param expire
     */
    public boolean setTTL(String key, int expire);
    
    /**
     * 获取指定的键设置过期日期
     *
     * @param key
     * @param timeUnit 
     */
    public Long getTTL(String key, TimeUnit timeUnit);
    
    /**
     * 尝试获得锁
     * @param lockName       锁名
     * @param acquireTimeout 获取锁的超时时间,单位为秒
     * @param keyTimeout     获取锁成功后键的过期时间,单位为秒.
     * @return identifier
     */
	public String tryLock(String lockName, long acquireTimeout, long keyTimeout);
	
	/**
	 * 释放锁
	 * @param lockName
	 * @param identifier
	 * @return
	 */
	public boolean releaseLock(String lockName, String identifier);
}

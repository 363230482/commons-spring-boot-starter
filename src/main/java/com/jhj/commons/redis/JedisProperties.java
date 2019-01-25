package com.jhj.commons.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbo
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "jedis")
public class JedisProperties {

    /**
     * 主机地址
     */
    private String host;
    /**
     * 端口号
     */
    private int port;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接池属性
     */
    private Pool pool;
    
    
    @Getter
    @Setter
    public static class Pool {

        /**
         * Max number of "idle" connections in the pool. Use a negative value to indicate
         * an unlimited number of idle connections.
         */
        private int maxIdle = 8;

        /**
         * Target for the minimum number of idle connections to maintain in the pool. This
         * setting only has an effect if it is positive.
         */
        private int minIdle = 0;

        /**
         * Max number of connections that can be allocated by the pool at a given time.
         * Use a negative value for no limit.
         */
        private int maxActive = 8;

        /**
         * Maximum amount of time (in milliseconds) a connection allocation should block
         * before throwing an exception when the pool is exhausted. Use a negative value
         * to block indefinitely.
         */
        private int maxWait = -1;
    }
}

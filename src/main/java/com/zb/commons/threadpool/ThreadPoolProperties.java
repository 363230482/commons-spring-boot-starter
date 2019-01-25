package com.zb.commons.threadpool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置属性类
 * 
 * @author Zhang Bo
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "threadpool")
public class ThreadPoolProperties {

    /**
     * 核心线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maxPoolSize;

    /**
     * 任务队列容量
     */
    private int queueCapacity;

    /**
     * 线程存活时间
     */
    private int keepAliveSeconds;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;
    
}

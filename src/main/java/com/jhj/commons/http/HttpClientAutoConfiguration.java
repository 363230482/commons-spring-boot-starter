package com.jhj.commons.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * http client 配置类
 * 
 * @author Lyu Yang, Zhang Bo
 *
 */
@Getter
@Setter
@Configuration
@EnableConfigurationProperties(value = HttpClientProperties.class)
@ConditionalOnProperty(prefix = "httpclient", value = "enabled", havingValue = "true")
public class HttpClientAutoConfiguration {
	
    @Autowired
    private HttpClientProperties httpClientProperties;
    
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public SimpleHttpClient simpleHttpClient() {
    	SimpleHttpClient simpleHttpClient = new SimpleHttpClient();
    	simpleHttpClient.setTimeout(httpClientProperties.getTimeout());
    	simpleHttpClient.setPoolSize(httpClientProperties.getPoolSize());
        simpleHttpClient.setLogLevel(httpClientProperties.getLogLevel());
        return simpleHttpClient;
    }

}

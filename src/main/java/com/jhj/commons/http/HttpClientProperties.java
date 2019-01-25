package com.jhj.commons.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangbo
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "httpclient")
public class HttpClientProperties {
    
    private int timeout = 60;

    private int poolSize = 30;
    
    private SimpleHttpClient.LogLevel logLevel = SimpleHttpClient.LogLevel.NONE;
    
}

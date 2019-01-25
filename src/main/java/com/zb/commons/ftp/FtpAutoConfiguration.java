package com.zb.commons.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Constructor;


/**
 * FTP Server 配置类
 * 
 * @author Zhang Bo
 */
@Configuration
@EnableConfigurationProperties(FtpProperties.class)
@ConditionalOnProperty(prefix = "ftp", value = "enabled", havingValue = "true")
public class FtpAutoConfiguration {
    
    @Autowired
    private FtpProperties ftpProperties;
    
    @Bean
    public FTPUtils ftpUtils() throws Exception {
        Class<FTPUtils> ftpUtilsClass = FTPUtils.class;
        Constructor<FTPUtils> constructor = ftpUtilsClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        FTPUtils ftpUtils = constructor.newInstance();
        ftpUtils.setIp(ftpProperties.getIp());
        ftpUtils.setPort(ftpProperties.getPort());
        ftpUtils.setUserName(ftpProperties.getUsername());
        ftpUtils.setPassword(ftpProperties.getPassword());
        ftpUtils.setUploadUrl(ftpProperties.getUploadUrl());

        return ftpUtils;
    }
    
}

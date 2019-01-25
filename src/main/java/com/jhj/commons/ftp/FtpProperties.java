package com.jhj.commons.ftp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbo
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {
    
    /**
     * IP地址
     */
    private String ip;
    /**
     * 端口号
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 上传根目录
     */
    private String uploadUrl;
    
}

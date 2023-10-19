package com.sky.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * :@ConfigurationProperties(prefix = "sky.jwt")将会绑定以"sky.jwt"作为前缀的属性值到JwtProperties这个类的实例中。
 * 也就是说，配置文件中的属性如sky.jwt.adminSecretKey、sky.jwt.adminTtl、sky.jwt.adminTokenName等会分别映射到JwtProperties类中的相应属性上。
 */
@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}

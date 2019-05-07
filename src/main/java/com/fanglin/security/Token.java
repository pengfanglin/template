package com.fanglin.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户授权信息
 * 如果在securityConfig中配置单点登录，需要重写equals和hashCode方法，否则单点登录会无效
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:44
 **/
@Data
@Accessors(chain = true)
public class Token implements UserDetails {
    /**
     * 用户主键id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否禁用
     */
    private boolean isDisable;
    /**
     * 授权时间
     */
    private Date tokenTime;
    /**
     * 权限集合
     */
    private List<GrantedAuthority> authorities;

    /**
     * 账号是否已过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return tokenTime.getTime() - System.currentTimeMillis() < 3600 * 1000;
    }

    /**
     * 账号是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isDisable;
    }

    /**
     * 授权是否已过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return tokenTime.getTime() - System.currentTimeMillis() < 3600 * 1000;
    }

    /**
     * 是否禁用
     */
    @Override
    public boolean isEnabled() {
        return !isDisable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return username.equals(token.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

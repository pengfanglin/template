package com.fanglin.entity.system;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统账号实体类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:40
 **/
@Data
@Accessors(chain = true)
@Table(name = "system_account")
public class SystemAccountEntity implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    /**
     * 主键
     */
    private Integer accountId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 是否禁用
     */
    private String isDisable;
    @Transient
    private String isDisableShow;
    /**
     * 角色id组合
     */
    private String roleIds;
    /**
     * 令牌
     */
    @Transient
    private String token;
    /**
     * 旧密码
     */
    @Transient
    private String systemOldPassword;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间
     */
    private Date updateTime;

    public SystemAccountEntity setIsDisable(String isDisable) {
        this.isDisable = isDisable;
        this.isDisableShow = "0".equals(isDisable) ? "正常" : "禁用";
        return this;
    }
}

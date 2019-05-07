package com.fanglin.entity.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 角色实体类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:38
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "role")
public class RoleEntity implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    /**
     * 主键
     */
    private Integer roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色标识
     */
    private String roleValue;
    /**
     * 禁用
     */
    private String isDisable;
    @Transient
    private String isDisableShow;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 角色拥有的模块组合
     */
    private String moduleIds;
    /**
     * 权限集合
     */
    private Set<AuthorityEntity> authorities;

    public RoleEntity setIsDisable(String isDisable) {
        this.isDisable = isDisable;
        this.isDisableShow = "0".equals(isDisable) ? "正常" : "禁用";
        return this;
    }
}

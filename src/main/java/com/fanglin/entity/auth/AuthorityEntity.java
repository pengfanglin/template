package com.fanglin.entity.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限实体类
 * @author 彭方林
 * @date 2019/4/3 16:29
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "authority")
public class AuthorityEntity implements Serializable {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long authorityId;
    /**
     * 权限名称
     */
    private String authorityName;
    /**
     * 权限标识
     */
    private String authorityValue;
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

    public AuthorityEntity setIsDisable(String isDisable) {
        this.isDisable = isDisable;
        this.isDisableShow="0".equals(isDisable)?"正常":"禁用";
        return this;
    }
}

package com.fanglin.entity.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

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
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
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
}

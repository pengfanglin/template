package com.fanglin.entity.system;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统模块实体类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:43
 **/
@Data
@Accessors(chain = true)
@Table(name = "system_module")
public class SystemModuleEntity implements Cloneable, Serializable {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer moduleId;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 模块访问路径
     */
    private String moduleUrl;
    /**
     * 父节点id
     */
    private Integer parentId;
    /**
     * 权重
     */
    private String sort;
    /**
     * 是否禁用
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
     * 子模块集合
     */
    private List<SystemModuleEntity> systemModules;

    public SystemModuleEntity setIsDisable(String isDisable) {
        this.isDisable = isDisable;
        this.isDisableShow = "0".equals(isDisable) ? "正常" : "禁用";
        return this;
    }
}

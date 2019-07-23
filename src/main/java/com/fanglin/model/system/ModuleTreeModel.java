package com.fanglin.model.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统模块树
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:43
 **/
@Setter
@Getter
@Accessors(chain = true)
@ApiModel(value = "系统模块树")
public class ModuleTreeModel {

    @ApiModelProperty(value = "id")
    private Integer moduleId;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "模块访问路径")
    private String moduleUrl;

    @ApiModelProperty(value = "父节点id")
    private Integer parentId;

    @ApiModelProperty(value = "权重")
    private Float sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "子模块")
    private List<ModuleTreeModel> childModules;
}

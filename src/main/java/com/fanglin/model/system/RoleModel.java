package com.fanglin.model.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 角色
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:38
 **/
@Setter
@Getter
@Accessors(chain = true)
@ApiModel(value = "角色")
public class RoleModel {

    @ApiModelProperty(value = "主键")
    private Integer roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleValue;

    @ApiModelProperty(value = "禁用")
    private String isDisable;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "角色拥有的模块组合")
    private String moduleIds;
}

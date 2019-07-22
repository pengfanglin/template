package com.fanglin.model.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 系统账号实体类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:40
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "系统账号")
public class AccountModel {

    @ApiModelProperty(value = "主键")
    private Integer accountId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "是否禁用")
    private String isDisable;

    @ApiModelProperty(value = "角色id组合")
    private String roleIds;
}

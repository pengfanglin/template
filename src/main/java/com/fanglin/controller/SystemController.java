package com.fanglin.controller;

import com.fanglin.core.page.PageResult;
import com.fanglin.core.others.Ajax;
import com.fanglin.core.page.Page;
import com.fanglin.model.system.AccountModel;
import com.fanglin.model.system.ModuleModel;
import com.fanglin.model.system.ModuleTreeModel;
import com.fanglin.model.system.RoleModel;
import com.fanglin.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/19 0:20
 **/
@RestController
@RequestMapping("/system/")
@Api(value = "/system/", tags = {"系统"})
public class SystemController extends BaseController {
    @Autowired
    SystemService systemService;

    @ApiOperation("系统账号详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "账号id", required = true)
    })
    @PostMapping("getSystemAccountDetail")
    public Ajax<AccountModel> getSystemAccountDetail(@RequestParam Integer id) {
        return Ajax.ok(systemService.getSystemAccountDetail(id));
    }

    @ApiOperation("系统账号列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "用户名"),
        @ApiImplicitParam(name = "isDisable", value = "是否禁用")
    })
    @PostMapping("getSystemAccountList")
    public Ajax<PageResult<AccountModel>> getSystemAccountList(String username, String isDisable, Page page) {
        return Ajax.ok(systemService.getSystemAccountList(username, isDisable, page));
    }

    @ApiOperation("修改系统账号")
    @PostMapping("updateSystemAccount")
    public Ajax updateSystemAccount(AccountModel systemAccount) {
        systemService.updateSystemAccount(systemAccount);
        return Ajax.ok();
    }

    @ApiOperation("添加系统账号")
    @PostMapping("insertSystemAccount")
    public Ajax insertSystemAccount(AccountModel systemAccount) {
        systemService.insertSystemAccount(systemAccount);
        return Ajax.ok();
    }

    @ApiOperation("删除系统账号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "账号id", required = true),
    })
    @PostMapping("deleteSystemAccount")
    public Ajax deleteSystemAccount(Integer id) {
        systemService.deleteSystemAccount(id);
        return Ajax.ok();
    }

    @ApiOperation("系统模块树")
    @PostMapping("getSystemModuleTree")
    public Ajax<List<ModuleTreeModel>> getSystemModuleTree() {
        return Ajax.ok(systemService.getSystemModuleTree());
    }

    @ApiOperation("左侧菜单树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "账号id", required = true),
    })
    @PostMapping("getLeftMenuTree")
    public Ajax<List<ModuleTreeModel>> getLeftMenuTree(@RequestParam Integer id) {
        return Ajax.ok(systemService.getLeftMenuTree(id));
    }

    @ApiOperation("系统模块列表")
    @PostMapping("getSystemModuleList")
    public Ajax<PageResult<ModuleModel>> getSystemModuleList(Page page, ModuleModel module) {
        return Ajax.ok(systemService.getSystemModuleList(module, page));
    }

    @ApiOperation("添加系统模块")
    @PostMapping("insertSystemModule")
    public Ajax insertSystemModule(ModuleModel module) {
        systemService.insertSystemModule(module);
        return Ajax.ok();
    }

    @ApiOperation("删除系统模块")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "模块id", required = true)
    })
    @PostMapping("deleteSystemModule")
    public Ajax deleteSystemModule(@RequestParam Integer id) {
        systemService.deleteSystemModule(id);
        return Ajax.ok();
    }

    @ApiOperation("修改系统模块")
    @PostMapping("updateSystemModule")
    public Ajax updateSystemModule(ModuleModel module) {
        systemService.updateSystemModule(module);
        return Ajax.ok();
    }

    @ApiOperation("模块详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "模块id", required = true)
    })
    @PostMapping("getSystemModuleDetail")
    public Ajax<ModuleModel> getSystemModuleDetail(@RequestParam Integer id) {
        return Ajax.ok(systemService.getSystemModuleDetail(id));
    }

    @ApiOperation("添加角色")
    @PostMapping("insertRole")
    public Ajax insertRole(RoleModel role) {
        systemService.insertRole(role);
        return Ajax.ok();
    }

    @ApiOperation("删除角色")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id", required = true)
    })
    @PostMapping("deleteRole")
    public Ajax deleteRole(@RequestParam Integer id) {
        systemService.deleteRole(id);
        return Ajax.ok();
    }

    @ApiOperation("修改角色")
    @PostMapping("updateRole")
    public Ajax updateRole(RoleModel role) {
        systemService.updateRole(role);
        return Ajax.ok();
    }

    @ApiOperation("角色详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id", required = true)
    })
    @PostMapping("getRoleDetail")
    public Ajax<RoleModel> getRoleDetail(@RequestParam Integer id) {
        return Ajax.ok(systemService.getRoleDetail(id));
    }

    @ApiOperation("角色列表")
    @PostMapping("getRoleList")
    public Ajax<PageResult<RoleModel>> getRoleList(Page page) {
        return Ajax.ok(systemService.getRoleList(page));
    }
}

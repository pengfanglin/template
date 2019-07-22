package com.fanglin.service;

import com.fanglin.core.page.Page;
import com.fanglin.core.page.PageResult;
import com.fanglin.model.system.ModuleModel;
import com.fanglin.model.system.ModuleTreeModel;
import com.fanglin.model.system.RoleModel;
import com.fanglin.model.system.AccountModel;

import java.util.List;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/4 10:00
 **/
public interface SystemService {
    /**
     * 系统模块树
     *
     * @return
     */
    List<ModuleTreeModel> getSystemModuleTree();

    /**
     * 系统账号左侧菜单树
     *
     * @param id 账号id
     * @return
     */
    List<ModuleTreeModel> getLeftMenuTree(Integer id);

    /**
     * 获取系统模块列表
     *
     * @param module
     * @param page
     * @return
     */
    PageResult<ModuleModel> getSystemModuleList(ModuleModel module, Page page);

    /**
     * 添加系统模块
     *
     * @param module
     */
    void insertSystemModule(ModuleModel module);

    /**
     * 删除系统模块
     *
     * @param id
     */
    void deleteSystemModule(Integer id);

    /**
     * 修改系统模块
     *
     * @param module
     */
    void updateSystemModule(ModuleModel module);

    /**
     * 系统模块详情
     *
     * @param id
     * @return
     */
    ModuleModel getSystemModuleDetail(Integer id);

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    PageResult<RoleModel> getRoleList(Page page);

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    RoleModel getRoleDetail(Integer id);

    /**
     * 修改角色信息
     *
     * @param role
     */
    void updateRole(RoleModel role);

    /**
     * 删除角色
     *
     * @param id
     */
    void deleteRole(Integer id);

    /**
     * 添加角色信息
     *
     * @param role
     */
    void insertRole(RoleModel role);

    /**
     * 账号详情
     *
     * @param id
     * @return
     */
    AccountModel getSystemAccountDetail(Integer id);

    /**
     * 系统账号列表
     *
     * @param username  用户名
     * @param isDisable 是否禁用
     * @param page      分页
     * @return
     */
    PageResult<AccountModel> getSystemAccountList(String username, String isDisable, Page page);

    /**
     * 修改系统账号
     *
     * @param account 系统账号
     */
    void updateSystemAccount(AccountModel account);

    /**
     * 添加系统账号
     *
     * @param account 账号
     */
    void insertSystemAccount(AccountModel account);

    /**
     * 删除系统账号
     *
     * @param id 账号id
     */
    void deleteSystemAccount(Integer id);
}

package com.fanglin.service;

import com.fanglin.core.others.Ajax;
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
    Ajax<List<ModuleTreeModel>> getSystemModuleTree();

    /**
     * 系统账号左侧菜单树
     *
     * @param id 账号id
     * @return
     */
    Ajax<List<ModuleTreeModel>> getLeftMenuTree(Integer id);

    /**
     * 获取系统模块列表
     *
     * @param module
     * @param page
     * @return
     */
    Ajax<PageResult<ModuleModel>> getSystemModuleList(ModuleModel module, Page page);

    /**
     * 添加系统模块
     *
     * @param module
     */
    int insertSystemModule(ModuleModel module);

    /**
     * 删除系统模块
     *
     * @param id
     */
    int deleteSystemModule(Integer id);

    /**
     * 修改系统模块
     *
     * @param module
     */
    int updateSystemModule(ModuleModel module);

    /**
     * 系统模块详情
     *
     * @param id
     * @return
     */
    Ajax<ModuleModel> getSystemModuleDetail(Integer id);

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    Ajax<PageResult<RoleModel>> getRoleList(Page page);

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    Ajax<RoleModel> getRoleDetail(Integer id);

    /**
     * 修改角色信息
     *
     * @param role
     */
    Ajax updateRole(RoleModel role);

    /**
     * 删除角色
     *
     * @param id
     */
    Ajax deleteRole(Integer id);

    /**
     * 添加角色信息
     *
     * @param role
     */
    Ajax insertRole(RoleModel role);

    /**
     * 账号详情
     *
     * @param id
     * @return
     */
    Ajax<AccountModel> getSystemAccountDetail(Integer id);

    /**
     * 系统账号列表
     *
     * @param username  用户名
     * @param isDisable 是否禁用
     * @param page      分页
     * @return
     */
    Ajax<PageResult<AccountModel>> getSystemAccountList(String username, String isDisable, Page page);

    /**
     * 修改系统账号
     *
     * @param account 系统账号
     */
    Ajax updateSystemAccount(AccountModel account);

    /**
     * 添加系统账号
     *
     * @param account 账号
     */
    Ajax insertSystemAccount(AccountModel account);

    /**
     * 删除系统账号
     *
     * @param id 账号id
     */
    Ajax deleteSystemAccount(Integer id);
}

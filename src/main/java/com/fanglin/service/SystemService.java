package com.fanglin.service;

import com.fanglin.entity.auth.RoleEntity;
import com.fanglin.entity.others.BannerEntity;
import com.fanglin.entity.others.HtmlStyleEntity;
import com.fanglin.entity.system.SystemAccountEntity;
import com.fanglin.entity.system.SystemHtmlEntity;
import com.fanglin.entity.system.SystemModuleEntity;
import com.fanglin.core.page.Page;
import com.fanglin.core.page.PageResult;
import org.apache.ibatis.annotations.Param;

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
    List<SystemModuleEntity> getSystemModuleTree();

    /**
     * 系统账号左侧菜单树
     *
     * @param accountId 账号id
     * @return
     */
    List<SystemModuleEntity> getLeftMenuTree(Integer accountId);

    /**
     * 获取系统模块列表
     *
     * @param systemModule
     * @param page
     * @return
     */
    PageResult<SystemModuleEntity> getSystemModuleList(SystemModuleEntity systemModule, Page page);

    /**
     * 添加系统模块
     *
     * @param systemModule
     */
    int insertSystemModule(SystemModuleEntity systemModule);

    /**
     * 删除系统模块
     *
     * @param moduleId
     */
    int deleteSystemModule(@Param("moduleId") Integer moduleId);

    /**
     * 修改系统模块
     *
     * @param systemModule
     */
    int updateSystemModule(SystemModuleEntity systemModule);

    /**
     * 系统模块详情
     *
     * @param moduleId
     * @return
     */
    SystemModuleEntity getSystemModuleDetail(@Param("moduleId") Integer moduleId);

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    PageResult<RoleEntity> getRoleList(Page page);

    /**
     * 角色详情
     *
     * @param roleId
     * @return
     */
    RoleEntity getRoleDetail(@Param("roleId") Integer roleId);

    /**
     * 修改角色信息
     *
     * @param role
     */
    int updateRole(RoleEntity role);

    /**
     * 删除角色
     *
     * @param roleId
     */
    int deleteRole(@Param("roleId") Integer roleId);

    /**
     * 添加角色信息
     *
     * @param role
     */
    int insertRole(RoleEntity role);

    /**
     * 添加系统html
     *
     * @param systemHtml
     */
    int insertHtml(SystemHtmlEntity systemHtml);

    /**
     * 删除系统html
     *
     * @param htmlId
     */
    int deleteHtml(@Param("htmlId") Integer htmlId);

    /**
     * 修改系统html
     *
     * @param systemHtml
     */
    int updateHtml(SystemHtmlEntity systemHtml);

    /**
     * 系统html详情
     *
     * @param htmlId
     * @return
     */
    SystemHtmlEntity getHtmlDetail(@Param("htmlId") Integer htmlId);

    /**
     * 系统html列表
     *
     * @param page
     * @return
     */
    PageResult<SystemHtmlEntity> getHtmlList(Page page);

    /**
     * 系统广告列表
     *
     * @param banner
     * @param page
     * @return
     */
    PageResult<BannerEntity> getBannerList(BannerEntity banner, Page page);

    /**
     * 系统广告详情
     *
     * @param bannerId
     * @return
     */
    BannerEntity getBannerDetail(@Param("accountId") Integer bannerId);

    /**
     * 修改系统广告
     *
     * @param banner
     */
    int updateBanner(BannerEntity banner);

    /**
     * 删除系统广告
     *
     * @param bannerId
     */
    int deleteBanner(@Param("accountId") Integer bannerId);

    /**
     * 添加系统广告
     *
     * @param banner
     */
    int insertBanner(BannerEntity banner);

    /**
     * 获取html样式模板
     *
     * @param htmlStyle
     * @return
     */
    HtmlStyleEntity getHtmlStyle(HtmlStyleEntity htmlStyle);

    /**
     * 账号详情
     *
     * @param accountId
     * @return
     */
    SystemAccountEntity getSystemAccountDetail(@Param("accountId") Integer accountId);

    /**
     * 系统账号列表
     *
     * @param systemAccount
     * @param page
     * @return
     */
    PageResult<SystemAccountEntity> getSystemAccountList(SystemAccountEntity systemAccount, Page page);

    /**
     * 修改系统账号
     *
     * @param systemAccount
     */
    int updateSystemAccount(SystemAccountEntity systemAccount);

    /**
     * 添加系统账号
     *
     * @param systemAccount
     */
    int insertSystemAccount(SystemAccountEntity systemAccount);

    /**
     * 删除系统账号
     *
     * @param accountId
     */
    int deleteSystemAccount(Integer accountId);
}

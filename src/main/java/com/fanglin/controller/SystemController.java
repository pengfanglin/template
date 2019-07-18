package com.fanglin.controller;

import com.fanglin.annotation.Token;
import com.fanglin.core.token.TokenInfo;
import com.fanglin.entity.auth.RoleEntity;
import com.fanglin.entity.others.BannerEntity;
import com.fanglin.entity.system.SystemModuleEntity;
import com.fanglin.core.others.Ajax;
import com.fanglin.entity.others.HtmlStyleEntity;
import com.fanglin.entity.system.SystemAccountEntity;
import com.fanglin.entity.system.SystemHtmlEntity;
import com.fanglin.core.page.Page;
import com.fanglin.service.SystemService;
import com.fanglin.utils.OthersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统权限控制器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/19 0:20
 **/
@RestController
@RequestMapping("/system/")
public class SystemController extends BaseController {
    @Autowired
    SystemService systemService;

    /**
     * 系统账号详情
     *
     * @param accountId
     * @return
     */
    @RequestMapping("getSystemAccountDetail")
    public Ajax getSystemAccountDetail(@RequestParam("accountId") Integer accountId) {
        return Ajax.ok(systemService.getSystemAccountDetail(accountId));
    }

    /**
     * 系统账号列表
     *
     * @param systemAccount
     * @param page
     * @return
     */
    @PostMapping("getSystemAccountList")
    public Ajax getSystemAccountList(SystemAccountEntity systemAccount, Page page) {
        return Ajax.ok(systemService.getSystemAccountList(systemAccount, page));
    }

    /**
     * 修改系统账号
     *
     * @param systemAccount
     * @return
     */
    @PostMapping("updateSystemAccount")
    public Ajax updateSystemAccount(SystemAccountEntity systemAccount) {
        systemService.updateSystemAccount(systemAccount);
        return Ajax.ok();
    }

    /**
     * 添加系统账号
     *
     * @param systemAccount
     * @return
     */
    @PostMapping("insertSystemAccount")
    public Ajax insertSystemAccount(SystemAccountEntity systemAccount) {
        systemService.insertSystemAccount(systemAccount);
        return Ajax.ok();
    }

    /**
     * 删除系统账号
     *
     * @param accountId
     * @return
     */
    @PostMapping("deleteSystemAccount")
    public Ajax deleteSystemAccount(Integer accountId) {
        if (systemService.deleteSystemAccount(accountId) > 0) {
            return Ajax.ok();
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 系统模块树
     *
     * @return
     */
    @PostMapping("getSystemModuleTree")
    public Ajax getSystemModuleTree(TokenInfo tokenInfo) {
        return Ajax.ok(systemService.getSystemModuleTree());
    }

    /**
     * 系统账号左侧菜单树
     *
     * @param accountId
     * @return
     */
    @PostMapping("getLeftMenuTree")
    public Ajax getLeftMenuTree(Integer accountId) {
        return Ajax.ok(systemService.getLeftMenuTree(accountId));
    }

    /**
     * 获取系统模块列表
     *
     * @param page
     * @param systemModule
     * @return
     */
    @PostMapping("getSystemModuleList")
    public Ajax getSystemModuleList(Page page, SystemModuleEntity systemModule) {
        return Ajax.ok(systemService.getSystemModuleList(systemModule, page));
    }

    /**
     * 添加系统模块
     *
     * @param systemModule
     * @return
     */
    @PostMapping("insertSystemModule")
    public Ajax insertSystemModule(SystemModuleEntity systemModule) {
        systemService.insertSystemModule(systemModule);
        return Ajax.ok("添加成功");
    }

    /**
     * 删除系统模块
     *
     * @param moduleId
     * @return
     */
    @PostMapping("deleteSystemModule")
    public Ajax deleteSystemModule(@RequestParam("moduleId") Integer moduleId) {
        systemService.deleteSystemModule(moduleId);
        return Ajax.ok("删除成功");
    }

    /**
     * 修改系统模块
     *
     * @param systemModule
     * @return
     */
    @PostMapping("updateSystemModule")
    public Ajax updateSystemModule(SystemModuleEntity systemModule) {
        systemService.updateSystemModule(systemModule);
        return Ajax.ok("修改成功");
    }

    /**
     * 单个系统模块详情
     *
     * @param moduleId
     * @return
     */
    @PostMapping("getSystemModuleDetail")
    public Ajax getSystemModuleDetail(@RequestParam("moduleId") Integer moduleId) {
        return Ajax.ok(systemService.getSystemModuleDetail(moduleId));
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @PostMapping("insertRole")
    public Ajax insertRole(RoleEntity role) {
        systemService.insertRole(role);
        return Ajax.ok("添加成功");
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @PostMapping("deleteRole")
    public Ajax deleteRole(@RequestParam("roleId") Integer roleId) {
        int num = systemService.deleteRole(roleId);
        System.out.println(num);
        return Ajax.ok("删除成功");
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @PostMapping("updateRole")
    public Ajax updateRole(RoleEntity role) {
        int num = systemService.updateRole(role);
        System.out.println(num);
        return Ajax.ok("修改成功");
    }

    /**
     * 角色详情
     *
     * @param roleId
     * @return
     */
    @PostMapping("getRoleDetail")
    public Ajax getRoleDetail(@RequestParam("roleId") Integer roleId) {
        return Ajax.ok(systemService.getRoleDetail(roleId));
    }

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    @PostMapping("getRoleList")
    public Ajax getRoleList(Page page) {
        return Ajax.ok(systemService.getRoleList(page));
    }

    /**
     * 添加系统html
     *
     * @param systemHtml
     * @return
     */
    @PostMapping("insertHtml")
    public Ajax insertHtml(SystemHtmlEntity systemHtml) {
        systemService.insertHtml(systemHtml);
        return Ajax.ok("操作成功");
    }

    /**
     * 删除系统html
     *
     * @param htmlId
     * @return
     */
    @PostMapping("deleteHtml")
    public Ajax deleteHtml(@RequestParam("htmlId") Integer htmlId) {
        systemService.deleteHtml(htmlId);
        return Ajax.ok("操作成功");
    }

    /**
     * 修改系统html
     *
     * @param systemHtml
     * @return
     */
    @PostMapping("updateHtml")
    public Ajax updateHtml(SystemHtmlEntity systemHtml) {
        systemService.updateHtml(systemHtml);
        return Ajax.ok("操作成功");
    }

    /**
     * 系统html详情
     *
     * @param htmlId
     * @return
     */
    @PostMapping("getHtmlDetail")
    public Ajax getHtmlDetail(@RequestParam("htmlId") Integer htmlId) {
        return Ajax.ok(systemService.getHtmlDetail(htmlId));
    }

    /**
     * 系统html列表
     *
     * @param page
     * @return
     */
    @PostMapping("getHtmlList")
    public Ajax getHtmlList(Page page) {
        return Ajax.ok(systemService.getHtmlList(page));
    }

    /**
     * 添加系统广告
     *
     * @param banner
     * @return
     */
    @PostMapping("insertBanner")
    public Ajax insertBanner(BannerEntity banner) {
        systemService.insertBanner(banner);
        return Ajax.ok("操作成功");
    }

    /**
     * 删除系统广告
     *
     * @param bannerId
     * @return
     */
    @PostMapping("deleteBanner")
    public Ajax deleteBanner(@RequestParam("bannerId") Integer bannerId) {
        systemService.deleteBanner(bannerId);
        return Ajax.ok("操作成功");
    }

    /**
     * 修改系统广告
     *
     * @param banner
     * @return
     */
    @PostMapping("updateBanner")
    public Ajax updateBanner(BannerEntity banner) {
        systemService.updateBanner(banner);
        return Ajax.ok("操作成功");
    }

    /**
     * 系统广告详情
     *
     * @param bannerId
     * @return
     */
    @PostMapping("getBannerDetail")
    public Ajax getBannerDetail(@RequestParam("bannerId") Integer bannerId) {
        return Ajax.ok(systemService.getBannerDetail(bannerId));
    }

    /**
     * 系统广告列表
     *
     * @param banner
     * @param page
     * @return
     */
    @PostMapping("getBannerList")
    public Ajax getBannerList(BannerEntity banner, Page page) {
        return Ajax.ok(systemService.getBannerList(banner, page));
    }

    /**
     * html文件内容
     *
     * @param url
     * @return
     */
    @PostMapping("getHtmlDesc")
    public Ajax getHtmlDesc(String url) {
        try {
            String desc = OthersUtils.readHtml(url);
            int start = desc.indexOf("<content>");
            int end = desc.lastIndexOf("</content>");
            if (start >= 0 && end >= 0) {
                desc = desc.substring(start + 9, end);
            }
            return Ajax.ok(desc);
        } catch (Exception e) {
            return Ajax.error("读取内容出现异常");
        }
    }

    /**
     * 修改html文件内容
     *
     * @param url
     * @param desc
     * @return
     */
    @PostMapping("setHtmlDesc")
    public Ajax setHtmlDesc(String url, String desc) {
        HtmlStyleEntity htmlStyle = systemService.getHtmlStyle(new HtmlStyleEntity().setStyleType("common"));
        if (OthersUtils.writeHtml(url, desc, htmlStyle.getStyleDesc())) {
            return Ajax.ok("保存成功");
        } else {
            return Ajax.error("保存失败");
        }
    }
}

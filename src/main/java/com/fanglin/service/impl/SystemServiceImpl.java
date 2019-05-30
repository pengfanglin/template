package com.fanglin.service.impl;

import com.fanglin.annotation.RedisCache;
import com.fanglin.core.others.Assert;
import com.fanglin.entity.auth.RoleEntity;
import com.fanglin.entity.others.BannerEntity;
import com.fanglin.entity.others.CodeEntity;
import com.fanglin.entity.others.HtmlStyleEntity;
import com.fanglin.entity.system.SystemAccountEntity;
import com.fanglin.entity.system.SystemHtmlEntity;
import com.fanglin.entity.system.SystemModuleEntity;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.core.page.Page;
import com.fanglin.core.page.PageResult;
import com.fanglin.core.others.ValidateException;
import com.fanglin.service.SystemService;
import com.fanglin.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 系统服务实现类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:44
 **/
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    MapperFactory mapperFactory;

    /**
     * 系统模块树
     *
     * @return
     */
    @RedisCache(value = "systemModuleTree", timeout = 1, unit = TimeUnit.HOURS)
    @Override
    public List<SystemModuleEntity> getSystemModuleTree() {
        return mapperFactory.othersMapper.getSystemModuleTree(null);
    }

    /**
     * 系统账号左侧菜单树
     *
     * @param accountId 账号id
     * @return
     */
    @Override
    public List<SystemModuleEntity> getLeftMenuTree(Integer accountId) {
        return mapperFactory.othersMapper.getSystemModuleTree(accountId);
    }

    /**
     * 获取系统模块列表
     *
     * @param systemModule
     * @param page
     * @return
     */
    @Override
    public PageResult<SystemModuleEntity> getSystemModuleList(SystemModuleEntity systemModule, Page page) {
        Example example = new Example(SystemModuleEntity.class);
        example.orderBy("sort").orderBy("moduleId");
        example.createCriteria().andEqualTo("parentId", systemModule.getParentId());
        List<SystemModuleEntity> systemModules = mapperFactory.systemModuleMapper.selectByExampleAndRowBounds(example, page);
        return new PageResult<>(systemModules, page.getTotal());
    }

    /**
     * 添加系统模块
     *
     * @param systemModule
     */
    @CacheEvict(key = "'systemModuleTree'", value = "systemModuleTree")
    @Override
    public int insertSystemModule(SystemModuleEntity systemModule) {
        if (systemModule.getParentId() == null) {
            systemModule.setParentId(0);
        }
        if (mapperFactory.systemModuleMapper.selectOne(new SystemModuleEntity().setModuleUrl(systemModule.getModuleUrl())) != null) {
            throw new ValidateException("路由重复");
        }
        return mapperFactory.systemModuleMapper.insertSelective(systemModule);
    }

    /**
     * 删除系统模块
     *
     * @param moduleId
     */
    @CacheEvict(key = "'systemModuleTree'", value = "systemModuleTree")
    @Override
    public int deleteSystemModule(Integer moduleId) {
        return mapperFactory.systemModuleMapper.deleteByPrimaryKey(moduleId);
    }

    /**
     * 修改系统模块
     *
     * @param systemModule
     */
    @CacheEvict(key = "'systemModuleTree'", value = "systemModuleTree")
    @Override
    public int updateSystemModule(SystemModuleEntity systemModule) {
        SystemModuleEntity systemModule1 = mapperFactory.systemModuleMapper.selectOne(new SystemModuleEntity().setModuleUrl(systemModule.getModuleUrl()));
        if (systemModule1 != null && !systemModule1.getModuleId().equals(systemModule.getModuleId())) {
            throw new ValidateException("路由重复");
        }
        return mapperFactory.systemModuleMapper.updateByPrimaryKeySelective(systemModule);
    }

    /**
     * 系统模块详情
     *
     * @param moduleId
     * @return
     */
    @Override
    public SystemModuleEntity getSystemModuleDetail(Integer moduleId) {
        return mapperFactory.systemModuleMapper.selectByPrimaryKey(moduleId);
    }

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    @Override
    public PageResult<RoleEntity> getRoleList(Page page) {
        Example example = new Example(RoleEntity.class);
        return new PageResult<>(mapperFactory.roleMapper.selectByExampleAndRowBounds(example, page), page.getTotal());
    }

    /**
     * 角色详情
     *
     * @param roleId
     * @return
     */
    @Override
    public RoleEntity getRoleDetail(Integer roleId) {
        return mapperFactory.roleMapper.selectByPrimaryKey(roleId);
    }

    /**
     * 修改角色信息
     *
     * @param role
     */
    @Override
    public int updateRole(RoleEntity role) {
        return mapperFactory.roleMapper.updateByPrimaryKeySelective(role);
    }

    /**
     * 删除角色
     *
     * @param roleId
     */
    @Override
    public int deleteRole(Integer roleId) {
        return mapperFactory.roleMapper.deleteByPrimaryKey(roleId);
    }

    /**
     * 添加角色信息
     *
     * @param role
     */
    @Override
    public int insertRole(RoleEntity role) {
        return mapperFactory.roleMapper.insertSelective(role);
    }

    /**
     * 添加系统html
     *
     * @param systemHtml
     */
    @Override
    public int insertHtml(SystemHtmlEntity systemHtml) {
        SystemHtmlEntity htmlBean2 = this.getHtmlDetail(systemHtml.getHtmlId());
        if (htmlBean2 != null) {
            throw new ValidateException("名称重复!");
        }
        int num = mapperFactory.systemHtmlMapper.insertSelective(systemHtml);
        this.writeHtmlUrl(systemHtml);
        return num;
    }

    /**
     * 删除系统html
     *
     * @param htmlId
     */
    @Override
    public int deleteHtml(Integer htmlId) {
        return mapperFactory.systemHtmlMapper.deleteByPrimaryKey(htmlId);
    }

    /**
     * 修改系统html
     *
     * @param systemHtml
     */
    @Override
    public int updateHtml(SystemHtmlEntity systemHtml) {
        this.writeHtmlUrl(systemHtml);
        return mapperFactory.systemHtmlMapper.updateByPrimaryKeySelective(systemHtml);
    }

    /**
     * 系统html详情
     *
     * @param htmlId
     * @return
     */
    @Override
    public SystemHtmlEntity getHtmlDetail(Integer htmlId) {
        return mapperFactory.systemHtmlMapper.selectByPrimaryKey(htmlId);
    }

    /**
     * 系统html列表
     *
     * @param page
     * @return
     */
    @Override
    public PageResult<SystemHtmlEntity> getHtmlList(Page page) {
        Example example = new Example(SystemHtmlEntity.class);
        return new PageResult<>(mapperFactory.systemHtmlMapper.selectByExampleAndRowBounds(example, page), page.getTotal());
    }

    /**
     * 系统广告列表
     *
     * @param banner
     * @param page
     * @return
     */
    @Override
    public PageResult<BannerEntity> getBannerList(BannerEntity banner, Page page) {
        return new PageResult<>(mapperFactory.othersMapper.getBannerList(banner, page), page.getTotal());
    }

    /**
     * 系统广告详情
     *
     * @param bannerId
     * @return
     */
    @Override
    public BannerEntity getBannerDetail(Integer bannerId) {
        return mapperFactory.bannerMapper.selectByPrimaryKey(bannerId);
    }

    /**
     * 修改系统广告
     *
     * @param banner
     */
    @Override
    public int updateBanner(BannerEntity banner) {
        this.writeBannerHtml(banner);
        return mapperFactory.bannerMapper.updateByPrimaryKeySelective(banner);
    }

    /**
     * 删除系统广告
     *
     * @param bannerId
     */
    @Override
    public int deleteBanner(Integer bannerId) {
        return mapperFactory.bannerMapper.deleteByPrimaryKey(bannerId);
    }

    /**
     * 添加系统广告
     *
     * @param banner
     */
    @Override
    public int insertBanner(BannerEntity banner) {
        this.writeBannerHtml(banner);
        return mapperFactory.bannerMapper.insertSelective(banner);
    }

    /**
     * 写入html富文本内容到文件中
     *
     * @param systemHtml
     */
    private void writeHtmlUrl(SystemHtmlEntity systemHtml) {
        HtmlStyleEntity htmlStyle = this.getHtmlStyle(new HtmlStyleEntity().setStyleType("common"));
        if (htmlStyle == null) {
            throw new ValidateException("html样式不能为空");
        }
        if (systemHtml.getHtmlUrlContent() != null) {
            if (systemHtml.getHtmlUrl() == null || "".equals(systemHtml.getHtmlUrl())) {
                String fileName = TimeUtils.getCurrentTime("yyyyMMddHHmmss") + new Random().nextInt(Integer.MAX_VALUE);
                String path = "/html/others/" + fileName + ".html";
                boolean result = OthersUtils.writeHtml(path, systemHtml.getHtmlUrlContent(), htmlStyle.getStyleDesc());
                if (result) {
                    systemHtml.setHtmlUrl(path);
                }
            } else {
                OthersUtils.writeHtml(systemHtml.getHtmlUrl(), systemHtml.getHtmlUrlContent(), htmlStyle.getStyleDesc());
            }
        }
    }

    /**
     * 写入轮播图富文本内容到文件中
     *
     * @param banner
     */
    private void writeBannerHtml(BannerEntity banner) {
        if ("common".equals(banner.getBannerType()) && banner.getBannerUrlContent() != null) {
            HtmlStyleEntity htmlStyle = this.getHtmlStyle(new HtmlStyleEntity().setStyleType("common"));
            if (htmlStyle == null) {
                throw new ValidateException("html样式不能为空");
            }
            if (banner.getBannerUrl() == null || "".equals(banner.getBannerUrl())) {
                String fileName = TimeUtils.getCurrentTime("yyyyMMddHHmmss") + new Random().nextInt(Integer.MAX_VALUE);
                String path = "/html/banner/" + fileName + ".html";
                boolean result = OthersUtils.writeHtml(path, banner.getBannerUrlContent(), htmlStyle.getStyleDesc());
                if (result) {
                    banner.setBannerUrl(path);
                }
            } else {
                OthersUtils.writeHtml(banner.getBannerUrl(), banner.getBannerUrlContent(), htmlStyle.getStyleDesc());
            }
        }
    }

    /**
     * 获取html样式模板
     *
     * @param htmlStyle
     * @return
     */
    @Override
    public HtmlStyleEntity getHtmlStyle(HtmlStyleEntity htmlStyle) {
        return mapperFactory.htmlStyleMapper.selectOne(htmlStyle);
    }

    /**
     * 系统账号详情
     *
     * @param accountId
     * @return
     */
    @Override
    public SystemAccountEntity getSystemAccountDetail(Integer accountId) {
        SystemAccountEntity systemAccount1 = mapperFactory.systemAccountMapper.selectByPrimaryKey(accountId);
        if (systemAccount1 != null) {
            systemAccount1.setPassword(null);
        }
        return systemAccount1;
    }

    /**
     * 系统账号列表
     *
     * @param systemAccount
     * @param page
     * @return
     */
    @Override
    public PageResult<SystemAccountEntity> getSystemAccountList(SystemAccountEntity systemAccount, Page page) {
        Example example = new Example(SystemAccountEntity.class);
        example.orderBy("accountId");
        Example.Criteria criteria = example.createCriteria();
        if (!OthersUtils.isEmpty(systemAccount.getUsername())) {
            criteria.andLike("username", "%" + systemAccount.getUsername() + "%");
        }
        if (!OthersUtils.isEmpty(systemAccount.getIsDisable())) {
            criteria.andEqualTo("isDisable", systemAccount.getIsDisable());
        }
        List<SystemAccountEntity> systemAccounts = mapperFactory.systemAccountMapper.selectByExampleAndRowBounds(example, page);
        for (SystemAccountEntity systemAccount1 : systemAccounts) {
            systemAccount1.setPassword(null);
        }
        return new PageResult<>(systemAccounts, page.getTotal());
    }

    /**
     * 修改系统账号
     *
     * @param systemAccount
     */
    @Override
    public int updateSystemAccount(SystemAccountEntity systemAccount) {
        SystemAccountEntity systemAccount1 = mapperFactory.systemAccountMapper.selectOne(new SystemAccountEntity().setUsername(systemAccount.getUsername()));
        if (systemAccount1 != null && !systemAccount1.getAccountId().equals(systemAccount.getAccountId())) {
            throw new ValidateException("账号已存在");
        }
        if (!OthersUtils.isEmpty(systemAccount.getPassword())) {
            systemAccount.setPassword(EncodeUtils.md5Encode(systemAccount.getPassword()));
        }
        return mapperFactory.systemAccountMapper.updateByPrimaryKeySelective(systemAccount);
    }

    /**
     * 添加系统账号
     *
     * @param systemAccount
     */
    @Override
    public int insertSystemAccount(SystemAccountEntity systemAccount) {
        if (mapperFactory.systemAccountMapper.select(new SystemAccountEntity().setUsername(systemAccount.getUsername())).size() > 0) {
            throw new ValidateException("账号已存在");
        }
        if (!OthersUtils.isEmpty(systemAccount.getPassword())) {
            systemAccount.setPassword(EncodeUtils.md5Encode(systemAccount.getPassword()));
        }
        return mapperFactory.systemAccountMapper.insertSelective(systemAccount);
    }

    /**
     * 删除系统账号
     *
     * @param accountId
     */
    @Override
    public int deleteSystemAccount(Integer accountId) {
        return mapperFactory.systemAccountMapper.deleteByPrimaryKey(accountId);
    }
}

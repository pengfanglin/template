package com.fanglin.service.impl;

import com.fanglin.common.annotation.RedisCache;
import com.fanglin.common.annotation.RedisCacheRemove;
import com.fanglin.common.core.others.Assert;
import com.fanglin.common.core.page.PageResult;
import com.fanglin.common.utils.BeanUtils;
import com.fanglin.common.utils.EncodeUtils;
import com.fanglin.common.utils.OthersUtils;
import com.fanglin.entity.system.RoleEntity;
import com.fanglin.entity.system.AccountEntity;
import com.fanglin.entity.system.ModuleEntity;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.core.page.Page;
import com.fanglin.model.system.AccountModel;
import com.fanglin.model.system.ModuleModel;
import com.fanglin.model.system.ModuleTreeModel;
import com.fanglin.model.system.RoleModel;
import com.fanglin.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @RedisCache(value = "system_module_tree", timeout = 1, unit = TimeUnit.HOURS)
    @Override
    public List<ModuleTreeModel> getSystemModuleTree() {
        return mapperFactory.others.getSystemModuleTree(null);
    }

    @Override
    public List<ModuleTreeModel> getLeftMenuTree(Integer accountId) {
        return mapperFactory.others.getSystemModuleTree(accountId);
    }

    @Override
    public PageResult<ModuleModel> getSystemModuleList(ModuleModel module, Page page) {
        return new PageResult<>(mapperFactory.module.getSystemModuleList(module, page), page.getTotal());
    }

    @RedisCacheRemove(value = "system_module_tree")
    @Override
    public void insertSystemModule(ModuleModel module) {
        Assert.isNull(mapperFactory.module.selectOne(new ModuleEntity().setModuleUrl(module.getModuleUrl())), "路由重复");
        mapperFactory.module.insertSelective(BeanUtils.copy(module, ModuleEntity.class));
    }

    @RedisCacheRemove(value = "system_module_tree")
    @Override
    public void deleteSystemModule(Integer moduleId) {
        mapperFactory.module.deleteByPrimaryKey(moduleId);
    }

    @RedisCacheRemove(value = "system_module_tree")
    @Override
    public void updateSystemModule(ModuleModel module) {
        ModuleEntity systemModule1 = mapperFactory.module.selectOne(new ModuleEntity().setModuleUrl(module.getModuleUrl()));
        Assert.isTrue(systemModule1 != null && !systemModule1.getModuleId().equals(module.getModuleId()), "路由重复");
        mapperFactory.module.updateByPrimaryKeySelective(BeanUtils.copy(module, ModuleEntity.class));
    }

    @Override
    public ModuleModel getSystemModuleDetail(Integer moduleId) {
        return BeanUtils.copy(mapperFactory.module.selectByPrimaryKey(moduleId), ModuleModel.class);
    }

    @Override
    public PageResult<RoleModel> getRoleList(Page page) {
        return new PageResult<>(mapperFactory.role.getRoleList(page), page.getTotal());
    }

    @Override
    public RoleModel getRoleDetail(Integer roleId) {
        return BeanUtils.copy(mapperFactory.role.selectByPrimaryKey(roleId), RoleModel.class);
    }

    @Override
    public void updateRole(RoleModel role) {
        mapperFactory.role.updateByPrimaryKeySelective(BeanUtils.copy(role, RoleEntity.class));
    }

    @Override
    public void deleteRole(Integer roleId) {
        mapperFactory.role.deleteByPrimaryKey(roleId);
    }

    @Override
    public void insertRole(RoleModel role) {
        mapperFactory.role.insertSelective(BeanUtils.copy(role, RoleEntity.class));
    }

    @Override
    public AccountModel getSystemAccountDetail(Integer accountId) {
        AccountEntity systemAccount1 = mapperFactory.account.selectByPrimaryKey(accountId);
        if (systemAccount1 != null) {
            systemAccount1.setPassword(null);
        }
        return BeanUtils.copy(systemAccount1, AccountModel.class);
    }

    @Override
    public PageResult<AccountModel> getSystemAccountList(String username, String isDisable, Page page) {
        List<AccountModel> accounts = mapperFactory.account.getSystemAccountList(username, isDisable, page);
        accounts.forEach(item -> {
            item.setPassword(null);
        });
        return new PageResult<>(accounts, page.getTotal());
    }

    @Override
    public void updateSystemAccount(AccountModel systemAccount) {
        AccountEntity systemAccount1 = mapperFactory.account.selectOne(new AccountEntity().setUsername(systemAccount.getUsername()));
        Assert.isTrue(systemAccount1 != null && !systemAccount1.getAccountId().equals(systemAccount.getAccountId()), "账号已存在");
        mapperFactory.account.updateByPrimaryKeySelective(BeanUtils.copy(systemAccount, AccountEntity.class));
    }

    @Override
    public void insertSystemAccount(AccountModel systemAccount) {
        Assert.isTrue(mapperFactory.account.select(new AccountEntity().setUsername(systemAccount.getUsername())).size() > 0, "账号已存在");
        if (OthersUtils.notEmpty(systemAccount.getPassword())) {
            systemAccount.setPassword(EncodeUtils.md5Encode(systemAccount.getPassword()));
        }
        mapperFactory.account.insertSelective(BeanUtils.copy(systemAccount, AccountEntity.class));
    }

    @Override
    public void deleteSystemAccount(Integer id) {
        Assert.isTrue(mapperFactory.account.deleteByPrimaryKey(id) > 0, "删除失败");
    }
}

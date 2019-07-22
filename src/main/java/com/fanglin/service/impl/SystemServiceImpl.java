package com.fanglin.service.impl;

import com.fanglin.annotation.LocalCache;
import com.fanglin.annotation.RedisCacheRemove;
import com.fanglin.entity.system.RoleEntity;
import com.fanglin.entity.system.AccountEntity;
import com.fanglin.entity.system.ModuleEntity;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.core.page.Page;
import com.fanglin.core.page.PageResult;
import com.fanglin.core.others.ValidateException;
import com.fanglin.model.system.AccountModel;
import com.fanglin.model.system.ModuleModel;
import com.fanglin.model.system.ModuleTreeModel;
import com.fanglin.model.system.RoleModel;
import com.fanglin.service.SystemService;
import com.fanglin.utils.*;
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
    @LocalCache(value = "systemModuleTree", timeout = 1, unit = TimeUnit.HOURS)
    @Override
    public List<ModuleTreeModel> getSystemModuleTree() {
        return mapperFactory.othersMapper.getSystemModuleTree(null);
    }

    @Override
    public List<ModuleTreeModel> getLeftMenuTree(Integer accountId) {
        return mapperFactory.othersMapper.getSystemModuleTree(accountId);
    }

    @Override
    public PageResult<ModuleModel> getSystemModuleList(ModuleModel module, Page page) {
        return new PageResult<>(mapperFactory.systemModuleMapper.getSystemModuleList(module, page), page.getTotal());
    }

    @RedisCacheRemove(value = "systemModuleTree")
    @Override
    public int insertSystemModule(ModuleModel module) {
        if (mapperFactory.systemModuleMapper.selectOne(new ModuleEntity().setModuleUrl(module.getModuleUrl())) != null) {
            throw new ValidateException("路由重复");
        }
        return mapperFactory.systemModuleMapper.insertSelective(BeanUtils.copy(module, ModuleEntity.class));
    }

    @RedisCacheRemove(value = "systemModuleTree")
    @Override
    public int deleteSystemModule(Integer moduleId) {
        return mapperFactory.systemModuleMapper.deleteByPrimaryKey(moduleId);
    }

    @RedisCacheRemove(value = "systemModuleTree")
    @Override
    public int updateSystemModule(ModuleModel module) {
        ModuleEntity systemModule1 = mapperFactory.systemModuleMapper.selectOne(new ModuleEntity().setModuleUrl(module.getModuleUrl()));
        if (systemModule1 != null && !systemModule1.getModuleId().equals(module.getModuleId())) {
            throw new ValidateException("路由重复");
        }
        return mapperFactory.systemModuleMapper.updateByPrimaryKeySelective(BeanUtils.copy(module, ModuleEntity.class));
    }

    @Override
    public ModuleModel getSystemModuleDetail(Integer moduleId) {
        return BeanUtils.copy(mapperFactory.systemModuleMapper.selectByPrimaryKey(moduleId), ModuleModel.class);
    }

    @Override
    public PageResult<RoleModel> getRoleList(Page page) {
        return new PageResult<>(mapperFactory.roleMapper.getRoleList(page), page.getTotal());
    }

    @Override
    public RoleModel getRoleDetail(Integer roleId) {
        return BeanUtils.copy(mapperFactory.roleMapper.selectByPrimaryKey(roleId), RoleModel.class);
    }

    @Override
    public int updateRole(RoleModel role) {
        return mapperFactory.roleMapper.updateByPrimaryKeySelective(BeanUtils.copy(role, RoleEntity.class));
    }

    @Override
    public int deleteRole(Integer roleId) {
        return mapperFactory.roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insertRole(RoleModel role) {
        return mapperFactory.roleMapper.insertSelective(BeanUtils.copy(role, RoleEntity.class));
    }

    @Override
    public AccountModel getSystemAccountDetail(Integer accountId) {
        AccountEntity systemAccount1 = mapperFactory.systemAccountMapper.selectByPrimaryKey(accountId);
        if (systemAccount1 != null) {
            systemAccount1.setPassword(null);
        }
        return BeanUtils.copy(systemAccount1, AccountModel.class);
    }

    @Override
    public PageResult<AccountModel> getSystemAccountList(String username, String isDisable, Page page) {
        List<AccountModel> accounts = mapperFactory.systemAccountMapper.getSystemAccountList(username, isDisable, page);
        accounts.forEach(item -> {
            item.setPassword(null);
        });
        return new PageResult<>(accounts, page.getTotal());
    }

    @Override
    public int updateSystemAccount(AccountModel systemAccount) {
        AccountEntity systemAccount1 = mapperFactory.systemAccountMapper.selectOne(new AccountEntity().setUsername(systemAccount.getUsername()));
        if (systemAccount1 != null && !systemAccount1.getAccountId().equals(systemAccount.getAccountId())) {
            throw new ValidateException("账号已存在");
        }
        return mapperFactory.systemAccountMapper.updateByPrimaryKeySelective(BeanUtils.copy(systemAccount, AccountEntity.class));
    }

    @Override
    public int insertSystemAccount(AccountModel systemAccount) {
        if (mapperFactory.systemAccountMapper.select(new AccountEntity().setUsername(systemAccount.getUsername())).size() > 0) {
            throw new ValidateException("账号已存在");
        }
        if (!OthersUtils.isEmpty(systemAccount.getPassword())) {
            systemAccount.setPassword(EncodeUtils.md5Encode(systemAccount.getPassword()));
        }
        return mapperFactory.systemAccountMapper.insertSelective(BeanUtils.copy(systemAccount, AccountEntity.class));
    }

    @Override
    public int deleteSystemAccount(Integer id) {
        return mapperFactory.systemAccountMapper.deleteByPrimaryKey(id);
    }
}

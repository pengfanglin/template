package com.fanglin.mapper;


import com.fanglin.entity.others.BannerEntity;
import com.fanglin.entity.system.SystemModuleEntity;
import com.fanglin.core.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 其他Mapper
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:36
 **/
public interface OthersMapper {
    /**
     * 系统账号左侧菜单树
     *
     * @param accountId 账号id
     * @return
     */
    List<SystemModuleEntity> getSystemModuleTree(@Param("accountId") Integer accountId);

    /**
     * 搜索banner列表
     *
     * @param banner
     * @param page
     * @return
     */
    List<BannerEntity> getBannerList(BannerEntity banner, Page page);

    /**
     * 根据roleIds查询角色和权限列表
     *
     * @param roleIds 角色id组合
     * @return
     */
    String getAuthInfo(String roleIds);
}

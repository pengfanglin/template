package com.fanglin.mapper;


import com.fanglin.model.system.ModuleTreeModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<ModuleTreeModel> getSystemModuleTree(@Param("accountId") Integer accountId);
}

package com.fanglin.mapper;

import com.fanglin.core.page.Page;
import com.fanglin.entity.system.ModuleEntity;
import com.fanglin.model.system.ModuleModel;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 系统模块
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:44
 **/
public interface SystemModuleMapper extends Mapper<ModuleEntity> {
    /**
     * 系统模块列表
     *
     * @param module
     * @param page
     * @return
     */
    @Select("select * from system_module where parent_id=#{parentId} order by sort,module_id")
    List<ModuleModel> getSystemModuleList(ModuleModel module, Page page);
}

package com.fanglin.mapper;

import com.fanglin.core.page.Page;
import com.fanglin.entity.system.RoleEntity;
import com.fanglin.model.system.RoleModel;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 角色
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:39
 **/
public interface RoleMapper extends Mapper<RoleEntity> {

    /**
     * 角色列表
     *
     * @param page
     * @return
     */
    @Select("select * from system_role order by role_id")
    List<RoleModel> getRoleList(Page page);
}

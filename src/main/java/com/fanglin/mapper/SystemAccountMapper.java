package com.fanglin.mapper;

import com.fanglin.core.page.Page;
import com.fanglin.entity.system.AccountEntity;
import com.fanglin.model.system.AccountModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 系统账号
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:41
 **/
public interface SystemAccountMapper extends Mapper<AccountEntity> {

    /**
     * 系统账号列表
     *
     * @param username  用户名
     * @param isDisable 是否禁用
     * @param page      分页
     * @return
     */
    List<AccountModel> getSystemAccountList(@Param("username") String username, @Param("isDisable") String isDisable, Page page);
}

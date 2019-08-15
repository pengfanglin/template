package com.fanglin.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用mapper的总仓库
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:34
 **/
@Component
public class MapperFactory {
    /**
     * 其他
     */
    @Autowired
    public OthersMapper others;
    /**
     * 账号
     */
    @Autowired
    public AccountMapper account;
    /**
     * 模块
     */
    @Autowired
    public ModuleMapper module;
    /**
     * 角色
     */
    @Autowired
    public RoleMapper role;
    /**
     * 支付记录
     **/
    @Autowired
    public PayHistoryMapper payHistory;
    /**
     * 退款记录
     **/
    @Autowired
    public RefundHistoryMapper refundHistory;
}

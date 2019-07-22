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
    public OthersMapper othersMapper;
    /**
     * 账号
     */
    @Autowired
    public AccountMapper accountMapper;
    /**
     * 模块
     */
    @Autowired
    public ModuleMapper moduleMapper;
    /**
     * 角色
     */
    @Autowired
    public RoleMapper roleMapper;
    /**
     * 支付记录
     **/
    @Autowired
    public PayHistoryMapper payHistoryMapper;
    /**
     * 退款记录
     **/
    @Autowired
    public RefundHistoryMapper refundHistoryMapper;
}

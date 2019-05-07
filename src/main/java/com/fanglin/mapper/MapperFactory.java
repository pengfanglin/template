package com.fanglin.mapper;

import com.fanglin.entity.pay.RefundHistoryEntity;
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
    @Autowired
    public OthersMapper othersMapper;
    @Autowired
    public SystemAccountMapper systemAccountMapper;
    @Autowired
    public SystemModuleMapper systemModuleMapper;
    @Autowired
    public RoleMapper roleMapper;
    @Autowired
    public SystemHtmlMapper systemHtmlMapper;
    @Autowired
    public HtmlStyleMapper htmlStyleMapper;
    @Autowired
    public BannerMapper bannerMapper;
    @Autowired
    public CodeMapper codeMapper;
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

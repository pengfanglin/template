package com.fanglin.mapper;

import com.fanglin.entity.pay.PayHistoryEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * 支付记录 Mapper 接口
 *
 * @author 彭方林
 * @date 2019-04-24
 */
public interface PayHistoryMapper extends Mapper<PayHistoryEntity> {
    /**
     * 累加支付记录已退款金额
     *
     * @param payId
     * @param refundAmount
     * @return
     */
    @Update("update pay_history set refund_amount=refund_amount+#{refundAmount} where payId=#{payId}")
    int addRefundAmount(@Param("payId") Long payId, @Param("refundAmount") String refundAmount);
}
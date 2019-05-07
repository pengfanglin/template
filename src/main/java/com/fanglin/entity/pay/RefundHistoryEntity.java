package com.fanglin.entity.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款记录
 *
 * @author 彭方林
 * @date 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "refund_history")
@ApiModel(value = "RefundHistoryEntity对象", description = "退款记录")
public class RefundHistoryEntity implements Serializable {

    @ApiModelProperty(value = "主键")
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long historyId;

    @ApiModelProperty(value = "创建人id")
    private Long creatorId;

    @ApiModelProperty(value = "支付记录id")
    private Long payId;

    @ApiModelProperty(value = "创建人类型")
    private Integer creatorType;

    @ApiModelProperty(value = "流水号")
    private String orderNo;

    @ApiModelProperty(value = "支付平台流水号")
    private String tradeNo;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "支付业务类型")
    private Integer payBusinessType;

    @ApiModelProperty(value = "状态 0-待确认 1-已退款")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "额外数据")
    private String extraData;
}

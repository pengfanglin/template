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
 * 支付记录
 *
 * @author 彭方林
 * @date 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "pay_history")
@ApiModel(value = "PayHistoryEntity对象", description = "支付记录")
public class PayHistoryEntity implements Serializable {

    @ApiModelProperty(value = "主键")
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long historyId;

    @ApiModelProperty(value = "创建人id")
    private Long creatorId;

    @ApiModelProperty(value = "创建人类型")
    private Integer creatorType;

    @ApiModelProperty(value = "流水号")
    private String orderNo;

    @ApiModelProperty(value = "支付平台流水号")
    private String tradeNo;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "支付方式")
    private Integer payWay;

    @ApiModelProperty(value = "支付平台支付类型")
    private String tradeType;

    @ApiModelProperty(value = "业务类型")
    private Integer payBusinessType;

    @ApiModelProperty(value = "支付平台额外数据")
    private String metadata;

    @ApiModelProperty(value = "状态 0-待支付 1-已支付")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "额外数据")
    private String extraData;
}

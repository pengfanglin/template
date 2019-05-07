package com.fanglin.entity.others;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.Order;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图
 * @author 彭方林
 * @date 2019/4/3 16:30
 * @version 1.0
 **/
@Data
@Accessors(chain = true)
@Table(name = "banner")
public class BannerEntity  implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Order
    private Integer bannerId;
    /**
     * 标题
     */
    private String bannerTitle;
    /**
     * 介绍
     */
    private String bannerDesc;
    /**
     * 图片
     */
    private String bannerImg;
    /**
     * 文件路径
     */
    private String bannerUrl;
    @Transient
    private String bannerUrlContent;
    /**
     * common:公共  chain:外链
     */
    private String bannerType;
    /**
     * 权重
     */
    private Float sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}

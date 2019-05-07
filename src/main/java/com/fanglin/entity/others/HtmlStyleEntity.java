package com.fanglin.entity.others;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * html模板
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:49
 **/
@Data
@Accessors(chain = true)
@Table(name = "html_style")
public class HtmlStyleEntity  implements Serializable {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer styleId;
    /**
     * 类型
     */
    private String styleType;
    /**
     * 内容
     */
    private String styleDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}

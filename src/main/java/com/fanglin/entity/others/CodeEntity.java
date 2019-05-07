package com.fanglin.entity.others;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 验证码
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:31
 **/
@Data
@Accessors(chain = true)
@Table(name = "code")
public class CodeEntity implements Serializable {
    /**
     * 主键
     */
    private Integer codeId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
    /**
     * 类型
     */
    private String codeType;
    /**
     * 内容
     */
    @Transient
    private String codeDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 过期时间
     */
    private Date effectiveTime;
}

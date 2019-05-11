package com.fanglin.core.token;

import com.fanglin.enums.others.TokenTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 令牌信息
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/5/11 22:23
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo implements Serializable {
    /**
     * 令牌生成时间
     */
    private Date tokenTime;
    /**
     * 令牌类型
     */
    private TokenTypeEnum type;
    /**
     * 用户的主键
     */
    private Integer id;
    /**
     * 生成的令牌
     */
    private String token;
}

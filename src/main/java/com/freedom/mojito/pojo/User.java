package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import java.time.LocalDateTime;

/**
 * Description: 用户信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@TableName(value ="user")
@Data
public class User {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户名
     */
    private String name;

    /**
     * 性别 0:女,1:男
     */
    @Digits(integer = 1, fraction = 0, message = "{user.sex.reg}")
    @Range(min = 0, max = 1, message = "{user.sex.reg}")
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态 0:禁用,1:正常
     */
    @Digits(integer = 1, fraction = 0, message = "{user.status.reg}")
    @Range(min = 0, max = 1, message = "{user.status.reg}")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
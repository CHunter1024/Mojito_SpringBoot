package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

@ApiModel("用户信息")
@TableName(value = "user")
@Data
public class User {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("性别 0:女,1:男")
    @Digits(integer = 1, fraction = 0, message = "{user.sex.reg}")
    @Range(min = 0, max = 1, message = "{user.sex.reg}")
    private Integer sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("状态 0:禁用,1:正常")
    @Digits(integer = 1, fraction = 0, message = "{user.status.reg}")
    @Range(min = 0, max = 1, message = "{user.status.reg}")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
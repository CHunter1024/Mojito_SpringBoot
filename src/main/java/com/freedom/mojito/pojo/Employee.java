package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Description: 员工信息
 * <p>CreateTime: 2022-07-12 下午 1:57</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("员工信息")
@TableName(value = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("帐号")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$", message = "{employee.account.reg}")
    private String account;

    @ApiModelProperty("密码")
    @Pattern(regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,18}$", message = "{employee.password.reg}")
    private String password;

    @ApiModelProperty("手机号码")
    @Pattern(regexp = "^1(3|4|5|6|7|8)\\d{9}$", message = "{employee.phoneNumber.reg}")
    private String phoneNumber;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("性别 1:男,0:女")
    @Digits(integer = 1, fraction = 0, message = "{employee.sex.reg}")
    @Range(min = 0, max = 1, message = "{employee.sex.reg}")
    private Integer sex;

    @ApiModelProperty("状态 0:禁用,1:正常")
    @Digits(integer = 1, fraction = 0, message = "{employee.status.reg}")
    @Range(min = 0, max = 1, message = "{employee.status.reg}")
    private Integer status;

    @ApiModelProperty("角色 0:普通员工,1:管理员")
    @Digits(integer = 1, fraction = 0, message = "{employee.role.reg}")
    @Range(min = 0, max = 1, message = "{employee.role.reg}")
    private Integer role;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充字段
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充字段
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}

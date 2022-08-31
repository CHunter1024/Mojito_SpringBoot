package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 员工信息
 * <p>CreateTime: 2022-07-12 下午 1:57</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@TableName(value = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 帐号
     */
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$", message = "{employee.account.reg}")
    private String account;

    /**
     * 密码
     */
    @Pattern(regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,18}$", message = "{employee.password.reg}")
    private String password;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1(3|4|5|6|7|8)\\d{9}$", message = "{employee.phoneNumber.reg}")
    private String phoneNumber;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别 1:男,0:女
     */
    @Digits(integer = 1, fraction = 0, message = "{employee.sex.reg}")
    @Range(min = 0, max = 1, message = "{employee.sex.reg}")
    private Integer sex;

    /**
     * 状态 0:禁用,1:正常
     */
    @Digits(integer = 1, fraction = 0, message = "{employee.status.reg}")
    @Range(min = 0, max = 1, message = "{employee.status.reg}")
    private Integer status;

    /**
     * 角色 0:普通员工,1:管理员
     */
    @Digits(integer = 1, fraction = 0, message = "{employee.role.reg}")
    @Range(min = 0, max = 1, message = "{employee.role.reg}")
    private Integer role;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充字段
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充字段
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}

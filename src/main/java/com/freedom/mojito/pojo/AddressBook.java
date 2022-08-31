package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 地址信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@TableName(value ="address_book")
@Data
public class AddressBook implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 9L;
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 性别 0:女,1:男
     */
    @Digits(integer = 1, fraction = 0, message = "{addressBook.sex.reg}")
    @Range(min = 0, max = 1, message = "{addressBook.sex.reg}")
    private Integer sex;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1(3|4|5|6|7|8)\\d{9}$", message = "{addressBook.phone.reg}")
    private String phone;

    /**
     * 省级区划编号
     */
    private String provinceCode;

    /**
     * 省级名称
     */
    private String provinceName;

    /**
     * 市级区划编号
     */
    private String cityCode;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 区级区划编号
     */
    private String districtCode;

    /**
     * 区级名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 标签
     */
    private String tag;

    /**
     * 默认 0:否,1:是
     */
    @Digits(integer = 1, fraction = 0, message = "{addressBook.isDefault.reg}")
    @Range(min = 0, max = 1, message = "{addressBook.isDefault.reg}")
    private Integer isDefault;

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

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

}
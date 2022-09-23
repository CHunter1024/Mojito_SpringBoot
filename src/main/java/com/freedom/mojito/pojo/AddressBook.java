package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Description: 地址信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("地址信息")
@TableName(value = "address_book")
@Data
public class AddressBook {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("性别 0:女,1:男")
    @Digits(integer = 1, fraction = 0, message = "{addressBook.sex.reg}")
    @Range(min = 0, max = 1, message = "{addressBook.sex.reg}")
    private Integer sex;

    @ApiModelProperty("手机号码")
    @Pattern(regexp = "^1(3|4|5|6|7|8)\\d{9}$", message = "{addressBook.phone.reg}")
    private String phone;

    @ApiModelProperty("省级区划编号")
    private String provinceCode;

    @ApiModelProperty("省级名称")
    private String provinceName;

    @ApiModelProperty("市级区划编号")
    private String cityCode;

    @ApiModelProperty("市级名称")
    private String cityName;

    @ApiModelProperty("区级区划编号")
    private String districtCode;

    @ApiModelProperty("区级名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("默认 0:否,1:是")
    @Digits(integer = 1, fraction = 0, message = "{addressBook.isDefault.reg}")
    @Range(min = 0, max = 1, message = "{addressBook.isDefault.reg}")
    private Integer isDefault;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除", hidden = true)
    @TableLogic
    private Integer isDeleted;

}
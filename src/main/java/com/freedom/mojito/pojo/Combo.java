package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 套餐信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("套餐信息")
@TableName(value = "combo")
@Data
public class Combo {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("套餐名称")
    private String name;

    @ApiModelProperty("套餐分类id")
    private Long categoryId;

    @ApiModelProperty("套餐价格")
    @Digits(integer = 8, fraction = 2, message = "{combo.price.reg}")
    @PositiveOrZero(message = "{combo.price.reg}")
    private BigDecimal price;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("状态 0:停售,1:起售")
    @Digits(integer = 1, fraction = 0, message = "{combo.status.reg}")
    @Range(min = 0, max = 1, message = "{combo.status.reg}")
    private Integer status;

    @ApiModelProperty("销量")
    private Integer sales;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @ApiModelProperty(value = "逻辑删除", hidden = true)
    @TableLogic
    private Integer isDeleted;

}
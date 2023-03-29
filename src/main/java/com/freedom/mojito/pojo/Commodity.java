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
 * Description: 商品信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("商品信息")
@TableName(value = "commodity")
@Data
public class Commodity {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类id")
    private Long categoryId;

    @ApiModelProperty("商品价格")
    @Digits(integer = 8, fraction = 2, message = "{commodity.price.reg}")
    @PositiveOrZero(message = "{commodity.price.reg}")
    private BigDecimal price;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("状态 0:停售,1:起售")
    @Digits(integer = 1, fraction = 0, message = "{commodity.status.reg}")
    @Range(min = 0, max = 1, message = "{commodity.status.reg}")
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
    private Boolean isDeleted;

}
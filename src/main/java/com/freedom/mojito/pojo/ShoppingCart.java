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
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 购物车
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("购物车")
@TableName(value = "shopping_cart")
@Data
public class ShoppingCart {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("类型 0:商品,1:套餐")
    @Digits(integer = 1, fraction = 0, message = "{shoppingCart.type.reg}")
    @Range(min = 0, max = 1, message = "{shoppingCart.type.reg}")
    private Integer type;

    @ApiModelProperty("名称(冗余字段)")
    private String name;

    @ApiModelProperty("图片(冗余字段)")
    private String image;

    @ApiModelProperty("规格")
    private String config;

    @ApiModelProperty("数量")
    @PositiveOrZero(message = "{shoppingCart.number.reg}")
    private Integer number;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
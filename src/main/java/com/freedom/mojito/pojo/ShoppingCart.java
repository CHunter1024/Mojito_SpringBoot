package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 购物车
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@TableName(value ="shopping_cart")
@Data
public class ShoppingCart implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 8L;
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
     * 商品id
     */
    private Long goodsId;

    /**
     * 类型 0:商品,1:套餐
     */
    @Digits(integer = 1, fraction = 0, message = "{shoppingCart.type.reg}")
    @Range(min = 0, max = 1, message = "{shoppingCart.type.reg}")
    private Integer type;

    /**
     * 名称(冗余字段)
     */
    private String name;

    /**
     * 图片(冗余字段)
     */
    private String image;

    /**
     * 规格
     */
    private String config;

    /**
     * 数量
     */
    @PositiveOrZero(message = "{shoppingCart.number.reg}")
    private Integer number;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
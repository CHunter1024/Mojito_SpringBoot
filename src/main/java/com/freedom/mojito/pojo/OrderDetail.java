package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 订单明细
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@TableName(value ="order_detail")
@Data
public class OrderDetail implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 11L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 类型 0:商品,1:套餐
     */
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
    private Integer number;

    /**
     * 金额
     */
    private BigDecimal amount;

}
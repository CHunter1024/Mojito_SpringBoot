package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description: 订单明细
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("订单明细")
@TableName(value = "order_detail")
@Data
public class OrderDetail {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("类型 0:商品,1:套餐")
    private Integer type;

    @ApiModelProperty("名称(冗余字段)")
    private String name;

    @ApiModelProperty("图片(冗余字段)")
    private String image;

    @ApiModelProperty("规格")
    private String config;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("金额")
    private BigDecimal amount;

}
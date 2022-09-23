package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 订单信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("订单信息")
@TableName(value = "`order`")
@Data
public class Order {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("地址id")
    private Long addressBookId;

    @ApiModelProperty("订单号")
    private String number;

    @ApiModelProperty("实收金额")
    private BigDecimal amount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("支付方式 1:微信,2:支付宝")
    @Digits(integer = 1, fraction = 0, message = "{order.payMethod.reg}")
    @Range(min = 1, max = 2, message = "{order.payMethod.reg}")
    private Integer payMethod;

    @ApiModelProperty("订单状态 1:待付款,2:已支付,3:已接单,4:派送中,5:已完成,6:已取消")
    @Digits(integer = 1, fraction = 0, message = "{order.status.reg}")
    @Range(min = 1, max = 6, message = "{order.status.reg}")
    private Integer status;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;

    @ApiModelProperty("支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty("接单时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("派送时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime finishTime;

    @ApiModelProperty("取消时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty("用户删除 0:未删除,1:已删除")
    private Integer userIsDeleted;

}
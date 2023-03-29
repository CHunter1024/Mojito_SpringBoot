package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 套餐商品关系
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("套餐商品关系")
@TableName(value = "combo_commodity")
@Data
public class ComboCommodity {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("套餐id")
    private Long comboId;

    @ApiModelProperty("商品id")
    private Long commodityId;

    @ApiModelProperty("商品名称(冗余字段)")
    private String commodityName;

    @ApiModelProperty("商品价格(冗余字段)")
    private BigDecimal commodityPrice;

    @ApiModelProperty("份数")
    private Integer copies;

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
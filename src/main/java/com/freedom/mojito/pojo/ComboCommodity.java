package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
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

@TableName(value ="combo_commodity")
@Data
public class ComboCommodity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 套餐id 
     */
    private Long comboId;

    /**
     * 商品id
     */
    private Long commodityId;

    /**
     * 商品名称(冗余字段)
     */
    private String commodityName;

    /**
     * 商品价格(冗余字段)
     */
    private BigDecimal commodityPrice;

    /**
     * 份数
     */
    private Integer copies;

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
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

}
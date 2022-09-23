package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 商品配置
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("商品配置")
@TableName(value = "commodity_config")
@Data
public class CommodityConfig {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("商品id")
    private Long commodityId;

    @ApiModelProperty("配置名称")
    private String name;

    @ApiModelProperty("配置内容")
    private String value;

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
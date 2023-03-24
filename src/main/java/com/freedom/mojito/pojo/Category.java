package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Description: 分类信息
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@ApiModel("分类信息")
@TableName(value = "category")
@Data
public class Category {

    @ApiModelProperty("主键")
    @TableId
    private Long id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类类型 0:商品分类,1:套餐分类")
    @Digits(integer = 1, fraction = 0, message = "{category.type.reg}")
    @Range(min = 0, max = 1, message = "{category.type.reg}")
    private Integer type;

    @ApiModelProperty("顺序")
    @Positive(message = "{category.sort.reg}")
    private Integer sort;

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

    /**
     * 逻辑删除
     * <p>由于除了主键还有字段(name)设置了唯一索引，所以需要将该字段和逻辑删除字段作为联合唯一索引</p>
     * <p>逻辑删除字段未删除值为0；已删除值为主键id值</p>
     */
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    @TableLogic(delval = "id")
    private Integer isDeleted;

}

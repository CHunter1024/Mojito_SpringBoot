package com.freedom.mojito.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 测试
 * @TableName temp
 */
@TableName(value ="temp")
@Data
public class Temp implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
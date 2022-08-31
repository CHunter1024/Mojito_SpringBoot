package com.freedom.mojito.dto;

import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.pojo.ComboCommodity;
import com.freedom.mojito.pojo.CommodityConfig;
import lombok.Data;

import java.util.List;

/**
 * Description: Combo的数据传输对象
 * <p>CreateTime: 2022-07-26 下午 11:11</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Data
public class ComboDto extends Combo {

    /**
     * 套餐商品
     */
    private List<ComboCommodity> commodities;

    /**
     * 套餐配置
     */
    private List<CommodityConfig> configs;

    /**
     * 分类Ids
     */
    private List<Long> categoryIds;

}

package com.freedom.mojito.dto;

import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.pojo.CommodityConfig;
import lombok.Data;

import java.util.List;

/**
 * Description: Commodity的数据传输对象
 * <p>CreateTime: 2022-07-26 下午 11:11</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Data
public class CommodityDto extends Commodity {

    /**
     * 商品配置
     */
    private List<CommodityConfig> configs;

    /**
     * 分类Ids
     */
    private List<Long> categoryIds;

}

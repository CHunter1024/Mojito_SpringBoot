package com.freedom.mojito.mapper;

import com.freedom.mojito.pojo.CommodityConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 针对表【commodity_config(商品配置)】的数据库操作Mapper
 * <p>CreateTime: 2022-07-20 上午 12:01</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Mapper
public interface CommodityConfigMapper extends BaseMapper<CommodityConfig> {

    /**
     * 根据ids和商品id删除商品配置
     *
     * @param ids
     * @param commodityId
     */
    // 注解版动态SQL需要在SQL语句中加入'<script></script>'标签
    @Delete("<script>" +
            "delete from commodity_config where commodity_id = #{commodityId}" +
            "<if test='!ids.isEmpty()'>" +
            "and id not in" +
            "<foreach collection='ids' item='id' separator=',' open='(' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    void trueDeleteByIdsAndCommodityId(@Param("ids") List<Long> ids, @Param("commodityId") Long commodityId);
}





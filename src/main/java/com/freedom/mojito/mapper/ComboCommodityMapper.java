package com.freedom.mojito.mapper;

import com.freedom.mojito.pojo.ComboCommodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 针对表【combo_commodity(套餐商品关系)】的数据库操作Mapper
 * <p>CreateTime: 2022-07-20 上午 12:01</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Mapper
public interface ComboCommodityMapper extends BaseMapper<ComboCommodity> {

    /**
     * 根据ids和套餐id删除套餐商品
     *
     * @param ids
     * @param comboId
     */
    // 注解版动态SQL需要在SQL语句中加入'<script></script>'标签
    @Delete("<script>" +
            "delete from combo_commodity where combo_id = #{comboId}" +
            "<if test='!ids.isEmpty()'>" +
            "and id not in" +
            "<foreach collection='ids' item='id' separator=',' open='(' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    void trueDeleteByIdsAndComboId(@Param("ids") List<Long> ids, @Param("comboId") Long comboId);
}





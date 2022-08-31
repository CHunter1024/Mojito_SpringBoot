package com.freedom.mojito.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedom.mojito.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 针对表【category(分类信息)】的数据库操作Mapper
 * <p>CreateTime: 2022-07-20 上午 12:01</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}

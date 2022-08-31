package com.freedom.mojito.mapper;

import com.freedom.mojito.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 针对表【user(用户信息)】的数据库操作Mapper
 * <p>CreateTime: 2022-07-12 下午 2:08</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}





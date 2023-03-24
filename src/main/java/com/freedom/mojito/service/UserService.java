package com.freedom.mojito.service;

import com.freedom.mojito.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * Description: 针对表【user(用户信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface UserService extends IService<User> {

    /**
     * 登录业务
     *
     * @param email
     * @return
     */
    User login(String email);

    /**
     * 更新用户信息并返回新的信息
     *
     * @param user
     * @return
     * @throws IOException
     */
    User updateUser(User user) throws IOException;

}

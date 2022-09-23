package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.mapper.UserMapper;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.UserService;
import com.freedom.mojito.util.FileUtils;
import com.freedom.mojito.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Description: 针对表【user(用户信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private FileUtils fileUtils;

    @Override
    public User login(String email) {
        // 根据email查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email).last("LIMIT 1");
        User user = getOne(wrapper);
        // 判断该邮箱地址对应的用户是否为新用户，如果是新用户就自动完成注册
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(RandomUtils.getStr(12));  // 随机生成用户名
            user.setStatus(1);
            save(user);
        }
        return user;
    }

    @Override
    public String encryptEmail(String email) {
        StringBuilder emailSB = new StringBuilder(email);
        int start = 3;  // 即保留前几位
        int end = emailSB.indexOf("@");
        for (int i = 0; i < end - start; i++) {
            emailSB.replace(start + i, start + (i + 1), "*");
        }
        return emailSB.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public User updateUser(User user) throws IOException {
        String oldAvatar = getById(user.getId()).getAvatar();
        String newAvatar = user.getAvatar();
        // 更新用户信息
        updateById(user);
        // 新旧头像图片处理
        fileUtils.handleOldNewFile(oldAvatar, newAvatar, "avatar");

        return getById(user.getId());
    }

    @Override
    public String updateEmailById(String email, Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        updateById(user);

        return encryptEmail(email);
    }
}





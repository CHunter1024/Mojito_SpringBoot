package com.freedom.mojito.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.UserService;
import com.freedom.mojito.util.EmailUtils;
import com.freedom.mojito.util.RandomUtils;
import com.freedom.mojito.util.ValidateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * <p>CreateTime: 2022-08-08 下午 3:05</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@RestController
@RequestMapping("/front/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailUtils emailUtils;

    /**
     * 根据邮箱地址生成验证码并发送出去
     *
     * @param email
     * @param session
     * @return
     */
    @GetMapping("/getCode/{email}")
    public Result<Object> sendCode(@PathVariable("email") String email, HttpSession session) {
        if (StringUtils.hasText(email)) {
            // 生成随机6位数字验证码
            String code = RandomUtils.getNumStr(6);
            log.info("验证码：{}", code);

            // 通过 Springboot mail 将验证码发送出去
//            emailUtils.sendCode(email, code);

            // 存放在session中，后期优化存放到redis中，并设置过期时间（如果过期时间还没到，则应该返回获取失败）
            session.setAttribute(email, code);
            return Result.succeed(null);
        }
        return Result.fail("参数有误");
    }

    /**
     * 用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody Map<String, String> map, HttpSession session) {
        String email = map.get("email");
        String code = map.get("code");
        // 从session中根据email获取验证码
        Object correctCode = session.getAttribute(email);

        if (correctCode == null) {
            return Result.fail("请先获取验证码");
        }
        if (!Objects.equals(code, correctCode)) {
            return Result.fail("验证码错误");
        }
        User user = userService.login(email);
        if (user.getStatus() == 0) {
            return Result.fail("该用户存在异常操作，已被锁定");
        }

        session.setAttribute("user", user);
        return Result.succeed(null);
    }

    /**
     * 获取当前用户信息
     *
     * @param session
     * @return
     */
    @GetMapping("/getCurrUser")
    public Result<User> getCurrUser(HttpSession session) {
        User currUser = (User) session.getAttribute("user");
        if (currUser != null) {
            User user = userService.getById(currUser.getId());
            user.setEmail(userService.encryptEmail(user.getEmail()));
            return Result.succeed(user);
        }
        return Result.fail("未登录");
    }

    /**
     * 用户登出
     *
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public Result<Object> logout(HttpSession session) {
        session.removeAttribute("user");
        return Result.succeed(null);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @param validResults
     * @param session
     * @return
     * @throws IOException
     */
    @PutMapping
    public Result<Object> updateUser(@RequestBody @Validated User user, BindingResult validResults, HttpSession session) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User currUser = (User) session.getAttribute("user");
        user.setId(currUser.getId());  // 保证修改的是当前用户信息
        currUser = userService.updateUser(user);
        session.setAttribute("user", currUser);  // 更新session中的用户信息
        return Result.succeed(null);
    }

    /**
     * 修改当前用户的Email
     *
     * @param map
     * @param session
     * @return
     */
    @PutMapping("/email")
    public Result<String> updateUserEmail(@RequestBody Map<String, String> map, HttpSession session) {
        String email = map.get("email");
        String code = map.get("code");
        Object correctCode = session.getAttribute(email);
        if (correctCode == null) {
            return Result.fail("请先获取验证码");
        }
        if (!Objects.equals(code, correctCode)) {
            return Result.fail("验证码错误");
        }

        User currUser = (User) session.getAttribute("user");
        if (Objects.equals(email, currUser.getEmail())) {
            return Result.fail("该邮箱地址已绑定当前帐号");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        if (userService.getOne(wrapper) != null) {
            return Result.fail("该邮箱地址已绑定其他帐号");
        }

        String enEmail = userService.updateEmailById(email, currUser.getId());
        currUser.setEmail(email);  // 更新session中的用户Email
        return Result.succeed(enEmail);
    }
}

package com.freedom.mojito.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.UserService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

@Api(tags = "用户相关API")
@Slf4j
@RestController
@RequestMapping("/front/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private HttpSession session;


    @ApiOperation(value = "用户登录", notes = "参数（json）：邮箱地址（email）/ 验证码（code)")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        String codeKey = email + "_login_code";
        String againKey = email + "_login_again";

        // 从redis中根据email获取验证码
        Object correctCode = redisTemplate.opsForValue().get(codeKey);
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

        // 登录成功，删除redis中的验证码和可再次获取时间，将用户信息存入session中
        redisTemplate.delete(codeKey);
        redisTemplate.delete(againKey);
        session.setAttribute("user", user);
        return Result.succeed(null);
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        session.removeAttribute("user");
        return Result.succeed(null);
    }

    @ApiOperation("查询用户是否已登录")
    @GetMapping("/isLogin")
    public Result<Boolean> isLogin() {
        return session.getAttribute("user") == null ? Result.succeed(false) : Result.succeed(true);
    }

    @ApiOperation("查询用户信息")
    @GetMapping
    public Result<User> getUser() {
        User user = (User) session.getAttribute("user");
        return Result.succeed(user);
    }

    @ApiOperation("修改用户信息")
    @PutMapping
    public Result<Object> updateUser(@RequestBody @Validated User user, BindingResult validResults) throws IOException {
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


    @ApiOperation(value = "修改用户的Email", notes = "参数（json）：邮箱地址（email）/ 验证码（code)")
    @PutMapping("/email")
    public Result<Object> updateUserEmail(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        String codeKey = email + "_update_code";
        String againKey = email + "_update_again";

        // 从redis中根据email获取验证码
        Object correctCode = redisTemplate.opsForValue().get(codeKey);
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

        currUser.setEmail(email);  // 更新session中的用户email
        userService.updateById(currUser);
        return Result.succeed(null);
    }
}

package com.freedom.mojito.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.UserService;
import com.freedom.mojito.util.EmailUtils;
import com.freedom.mojito.util.RandomUtils;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private EmailUtils emailUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private HttpSession session;


    @ApiOperation("根据邮箱地址生成验证码并发送出去")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱地址", required = true, paramType = "path", dataTypeClass = String.class)
    })
    @GetMapping("/getCode/{email}")
    public Result<Object> sendCode(@PathVariable("email") String email) {
        if (StringUtils.hasText(email)) {
            String codeKey = email + "_code";
            String againKey = email + "_again";
            // 检查该邮箱地址在60秒内是否已获取过验证码
            if (redisTemplate.opsForValue().get(againKey) != null) {
                return Result.fail("操作过于频繁，请60秒后重试");
            }

            // 生成随机6位数字验证码
            String code = RandomUtils.getNumStr(6);
            log.info("验证码：{}", code);

            // 通过 Springboot mail 将验证码发送出去
            emailUtils.sendCode(email, code);

            // 存放到redis中，并设置验证码过期时间和可再次获取时间
            redisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(againKey, "", 60, TimeUnit.SECONDS);
            return Result.succeed(null);
        }
        return Result.fail("参数有误");
    }


    @ApiOperation(value = "用户登录", notes = "参数（json）：邮箱地址（email）/ 验证码（code)")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        // 从redis中根据email获取验证码
        String codeKey = email + "_code";
        Object correctCode = redisTemplate.opsForValue().get(codeKey);

        if (correctCode == null) {
            return Result.fail("验证码已过期，请重新获取");
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


    @ApiOperation("获取当前用户信息")
    @GetMapping("/getCurrUser")
    public Result<User> getCurrUser() {
        User currUser = (User) session.getAttribute("user");
        if (currUser != null) {
            User user = userService.getById(currUser.getId());
            user.setEmail(userService.encryptEmail(user.getEmail()));
            return Result.succeed(user);
        }
        return Result.fail("未登录");
    }


    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        session.removeAttribute("user");
        return Result.succeed(null);
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


    @ApiOperation(value = "修改当前用户的Email", notes = "参数（json）：邮箱地址（email）/ 验证码（code)")
    @PutMapping("/email")
    public Result<String> updateUserEmail(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        // 从redis中根据email获取验证码
        String codeKey = email + "_code";
        Object correctCode = redisTemplate.opsForValue().get(codeKey);
        if (correctCode == null) {
            return Result.fail("验证码已过期，请重新获取");
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

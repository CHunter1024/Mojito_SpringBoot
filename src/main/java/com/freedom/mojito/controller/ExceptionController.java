package com.freedom.mojito.controller;

import com.freedom.mojito.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * Description: 统一异常处理器
 * <p>CreateTime: 2022-07-14 下午 11:28</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    /**
     * 处理未知异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    private Result<Object> handleException(Exception exception) {
        log.error("服务出错了，错误类型：{}", exception.getClass().toString());
        exception.printStackTrace();
        return Result.fail("未知错误");
    }

    /**
     * 处理属性值重复异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    private Result<Object> handleDuplicateKeyException(Exception exception) {
        log.error("服务出错了，错误类型：{}", exception.getClass().toString());
        exception.printStackTrace();
        String errMsg = exception.getMessage();
        if (errMsg.contains("employee.account_unique")) {
            return Result.fail("帐号已存在");
        }
        if (errMsg.contains("employee.phone_number_unique")) {
            return Result.fail("手机号码已存在");
        }
        if (errMsg.contains("category.name_is_deleted_unique")) {
            return Result.fail("该分类已存在");
        }
        return Result.fail("参数值已存在");
    }

    /**
     * 处理邮件发送异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MailSendException.class)
    private Result<Object> handleMailSendException(Exception exception) {
        log.error("服务出错了，错误类型：{}", exception.getClass().toString());
        exception.printStackTrace();
        return Result.fail("验证码发送失败");
    }
}

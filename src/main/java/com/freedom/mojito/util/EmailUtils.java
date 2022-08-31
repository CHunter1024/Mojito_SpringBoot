package com.freedom.mojito.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Description: 邮件发送工具类
 * <p>CreateTime: 2022-08-05 上午 11:57</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@Component
public class EmailUtils {

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${message.subject}")
    private String subject;  // 主题
    @Value("${message.template}")
    private String template;  // 内容模板

    /**
     * 发送验证码文本邮件
     *
     * @param email 收件人
     * @param code  验证码
     */
    public void sendCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailProperties.getUsername());  // 发送人
        message.setTo(email);  // 收件人
        message.setSubject(subject);  // 标题
        message.setText(template.replace("{}", code));  // 内容

        javaMailSender.send(message);
        log.info("邮件已发送给：{}", email);
    }
}

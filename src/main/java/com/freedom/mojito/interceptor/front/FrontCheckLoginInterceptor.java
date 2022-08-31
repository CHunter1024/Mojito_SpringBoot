package com.freedom.mojito.interceptor.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Employee;
import com.freedom.mojito.pojo.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: 检查登录拦截器
 * <p>CreateTime: 2022-07-12 下午 10:09</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@ConfigurationProperties("interceptor.front.check-login-interceptor")
@Data
public class FrontCheckLoginInterceptor implements HandlerInterceptor {

    /**
     * 拦截的路径
     */
    private String[] interceptPaths = {};

    /**
     * 排除的路径
     */
    private String[] excludePaths = {};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("front--检查登录拦截器拦截到请求：{}", request.getRequestURI());

        // 判断登录状态，如果已登录，则直接放行
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            log.info("用户已登录，用户id为：{}，已放行", user.getId());
            return true;
        }

        log.warn("用户未登录，重定向到登录页面");
        // 重定向到登录页面
        if (request.getRequestURI().endsWith(".html")) {
            response.sendRedirect("/front/page/login.html");
        } else {
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.fail("not logged in")));
        }

        return false;
    }
}

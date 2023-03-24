package com.freedom.mojito.interceptor.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Employee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMethod;
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
@ConfigurationProperties("interceptor.backend.check-login-interceptor")
@Data
public class BackendCheckLoginInterceptor implements HandlerInterceptor {

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
        // 放行预检请求
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        log.info("backend--检查登录拦截器拦截到请求：{}", request.getRequestURI());

        // 判断登录状态，如果已登录，则直接放行
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        if (employee != null) {
            log.info("用户已登录，用户id为：{}，已放行", employee.getId());
            return true;
        }

        log.warn("用户未登录");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.notLogin("未登录")));
        return false;
    }
}

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
 * Description: 权限拦截器
 * <p>CreateTime: 2022-07-15 下午 6:25</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Slf4j
@ConfigurationProperties("interceptor.backend.permission-interceptor")
@Data
public class PermissionInterceptor implements HandlerInterceptor {

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

        log.info("backend--权限拦截器拦截到请求：{}", request.getRequestURI());

        // 判断是否为管理员
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        if (employee != null && employee.getRole() == 1) {
            log.info("该用户是管理员，已放行");
            return true;
        }

        log.warn("该用户不是管理员，已拦截");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.fail("没有权限访问！")));
        return false;
    }
}

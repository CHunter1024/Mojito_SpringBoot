package com.freedom.mojito.config;

import com.freedom.mojito.common.JacksonObjectMapper;
import com.freedom.mojito.interceptor.backend.BackendCheckLoginInterceptor;
import com.freedom.mojito.interceptor.backend.PermissionInterceptor;
import com.freedom.mojito.interceptor.front.FrontCheckLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-07-12 下午 10:19</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Configuration
@EnableConfigurationProperties({BackendCheckLoginInterceptor.class, PermissionInterceptor.class, FrontCheckLoginInterceptor.class})
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BackendCheckLoginInterceptor backendCheckLoginInterceptor;

    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Autowired
    private FrontCheckLoginInterceptor frontCheckLoginInterceptor;

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加Backend检查登录拦截器
        registry.addInterceptor(backendCheckLoginInterceptor).addPathPatterns(backendCheckLoginInterceptor.getInterceptPaths())
                .excludePathPatterns(backendCheckLoginInterceptor.getExcludePaths()).order(1);

        // 添加权限拦截器
        registry.addInterceptor(permissionInterceptor).addPathPatterns(permissionInterceptor.getInterceptPaths())
                .excludePathPatterns(permissionInterceptor.getExcludePaths()).order(2);

        // 添加Front检查登录拦截器
        registry.addInterceptor(frontCheckLoginInterceptor).addPathPatterns(frontCheckLoginInterceptor.getInterceptPaths())
                .excludePathPatterns(frontCheckLoginInterceptor.getExcludePaths()).order(1);
    }

    /**
     * 扩展MVC框架的消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象映射器，底层使用Jackson将Java对象转为Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象添加到MVC框架的转换器集合中，并放在首位
        converters.add(0, messageConverter);
    }

    /**
     * 配置全局跨域请求处理（具体就是修改响应头）
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加映射路径（/**，处理所有的请求）
        registry.addMapping("/**")
                // 指定放行域，（http://127.0.0.1:[*]，ip为127.0.0.1下的任意端口）
                .allowedOriginPatterns("http://127.0.0.1:[*]")
                // 是否允许发送Cookie
                .allowCredentials(true)
                // 指定允许的请求方式
                .allowedMethods("*")
                // 指定允许携带的请求头
                .allowedHeaders("*")
                // 指定暴露的请求头
                .exposedHeaders("*");
    }
}

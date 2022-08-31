package com.freedom.mojito.config;

import com.freedom.mojito.common.JacksonObjectMapper;
import com.freedom.mojito.interceptor.backend.BackendCheckLoginInterceptor;
import com.freedom.mojito.interceptor.backend.PermissionInterceptor;
import com.freedom.mojito.interceptor.front.FrontCheckLoginInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        // 设置对象转换器，底层使用Jackson将Java对象转为Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象添加到MVC框架的转换器集合中，并放在首位
        converters.add(0, messageConverter);
    }

    /**
     * 扩展MVC框架的参数转换器
     *
     * @param registry
     */
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter((Converter<String, LocalDateTime>) source -> LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        registry.addConverter((Converter<String, LocalDate>) source -> LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        registry.addConverter((Converter<String, LocalTime>) source -> LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss")));
//    }
}

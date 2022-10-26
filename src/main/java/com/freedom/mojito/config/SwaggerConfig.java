package com.freedom.mojito.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description:
 * <p>CreateTime: 2022-09-21 下午 4:16</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@EnableSwagger2
@EnableKnife4j
@Configuration
public class SwaggerConfig {

    /**
     * Swagger-API文档配置
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo())
                .groupName("Mojito-API")
                // 接口扫描
                .select()
                // 方式一: 扫描指定包下的Controller
//                .apis(RequestHandlerSelectors.basePackage("com.freedom.mojito.controller"))
                // 方式二：扫描有配置指定注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API文档信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mojito接口文档")
                .description("此文档为Mojito项目中所有接口的定义说明")
                .termsOfServiceUrl("http://localhost:8080")
                .contact(new Contact("freedom", "", "2396598264@qq.com"))
                .version("springboot")
                .build();
    }

}

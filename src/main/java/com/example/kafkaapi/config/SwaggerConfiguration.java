package com.example.kafkaapi.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author grassPrince
 * @Date 2020/7/14 8:52
 * @Description TODO
 **/

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    @Bean(value = "api")
    public Docket api() {
        Docket docket=new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("api")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.kafkaapi.controller.api"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean(value = "spring")
    public Docket spring() {
        Docket docket=new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("spring")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.kafkaapi.controller.spring"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("kafka-api")
                .description("根据topic自动化部署connector和table")
                .termsOfServiceUrl("http://localhost:8091/kafka-api/doc.html")
                .version("1.0")
                .build();
    }

}

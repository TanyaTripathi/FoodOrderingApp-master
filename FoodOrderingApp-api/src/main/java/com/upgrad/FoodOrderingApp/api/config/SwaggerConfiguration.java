package com.upgrad.FoodOrderingApp.api.config;


import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This Configuration integrates Swagger2 into the existing Spring Boot project.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    ApiInfo apiInfo = new ApiInfoBuilder().title("Foodies Junction : IIIT-B Capstone Project (Back-end)").description("API end-points for Foodies Junction Application").
            version("1.0.1-SNAPSHOT").build();

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.upgrad.FoodOrderingApp.api.controller")).
                paths(PathSelectors.any()).build();
    }
}
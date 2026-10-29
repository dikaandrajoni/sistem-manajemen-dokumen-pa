//package com.proyekakhir.manajemendokumen.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//public class SwaggerConfig {
//
//    public static final String TYPE = "string";
//    public static final String PARAM_TYPE = "header";
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .globalOperationParameters(Arrays.asList(
//                        new ParameterBuilder()
//                                .name("User-ID")
//                                .description("Logged in User ID")
//                                .modelRef(new ModelRef(TYPE))
//                                .parameterType(PARAM_TYPE)
//                                .required(true)
//                                .build()
//                ))
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.regex("/bpmn.*|/crud.*"))
//                .build();
//    }
//}

package com.trip.mbti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     WebMvcConfigurer.super.addInterceptors(registry);
    // }

    /*
    정적리소스 경로 설정
    */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
     }

    // @Override
    // public void configureViewResolvers(ViewResolverRegistry registry) {
    //     registry.jsp("/WEB-INF/jsp/", ".jsp");
    
    // }
    
    // gatewayServer cors 설정 후 중복 cors 체크 방지를 위해 주석처리
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    // }

}

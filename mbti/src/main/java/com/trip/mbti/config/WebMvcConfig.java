package com.trip.mbti.config;

import org.springframework.context.annotation.Configuration;
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

}

package com.trip.mbti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
public CorsFilter corsFilter(){

    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    //config.setAllowCredentials(true); //addAllowedOrigin 동시사용 불가
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.setMaxAge(3600L);
    config.addExposedHeader("Authorization");
    src.registerCorsConfiguration("/**", config);
    return new CorsFilter(src);

}
}

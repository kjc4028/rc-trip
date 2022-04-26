package com.trip.mbti.config;

import com.trip.mbti.MbtiApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 추후 war 형태로 배포하기 위해 상속
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MbtiApplication.class);
    }
}

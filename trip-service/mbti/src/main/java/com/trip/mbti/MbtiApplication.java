package com.trip.mbti;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableRedisRepositories
public class MbtiApplication {

	@Value("${profileValue}")
	String profileValue;

	public static void main(String[] args) {
		SpringApplication.run(MbtiApplication.class, args);
	}

	@PostConstruct
    private void start() {
        //System.out.println(">>>>>>>>>>><<<><><><><><>>profileValue = " + profileValue);
    }
}

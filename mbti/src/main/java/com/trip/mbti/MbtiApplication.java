package com.trip.mbti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MbtiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MbtiApplication.class, args);
	}

}

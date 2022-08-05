package com.trip.mbti.rest.trip;

import java.io.ObjectInputFilter.Config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="user-msa-instance", configuration = Config.class)
public interface UserServiceClient {
    
    @GetMapping("/user/claim/{claimKey}")
    String getClaim(@PathVariable String claimKey);
}

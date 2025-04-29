package com.trip.info.rest.trip;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.trip.info.rest.common.Message;

@FeignClient(name="USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/user/claim/{claimKey}")
    ResponseEntity<Message> getClaim(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable("claimKey") String claimKey); 
}

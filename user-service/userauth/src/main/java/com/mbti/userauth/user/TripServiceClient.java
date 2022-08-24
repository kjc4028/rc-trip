package com.mbti.userauth.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name="TRIP-SERVICE")
public interface TripServiceClient {
    
    @DeleteMapping(path="/trips/user")
    @ResponseBody
    public void tripDeleteBuRegUserId(@RequestBody TripDto tripDto);
}

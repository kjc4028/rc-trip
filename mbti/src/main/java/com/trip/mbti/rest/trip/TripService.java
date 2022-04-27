package com.trip.mbti.rest.trip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public List<TripEntity> findAllTripEntity() {
       return tripRepository.findAll(); 
    }
}

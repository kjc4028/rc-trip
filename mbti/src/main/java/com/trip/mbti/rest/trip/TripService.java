package com.trip.mbti.rest.trip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public List<TripEntity> findAllTripEntity() {
       return tripRepository.findAll(); 
    }

    public Page<TripEntity> findAllTripPage(int pageNum, int perPage){
        return tripRepository.findAll(PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public void save(TripEntity tripEntity){
        tripRepository.save(tripEntity);
    }
    
    public void delete(TripEntity tripEntity){
        tripRepository.delete(tripEntity);
    }
}

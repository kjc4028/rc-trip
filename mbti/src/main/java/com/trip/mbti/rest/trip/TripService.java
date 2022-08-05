package com.trip.mbti.rest.trip;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    private UserServiceClient userServiceClient;


    public void UserServiceClient(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public Optional<TripEntity> findOneById(String id) {
        return tripRepository.findById(id); 
     }

    public List<TripEntity> findAllTripEntity() {
       return tripRepository.findAll(); 
    }

    public Page<TripEntity> findAllTripPage(int pageNum, int perPage){
        return tripRepository.findAll(PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public Page<TripEntity> findSearchTripNmPage(TripEntity tripEntity, int pageNum, int perPage){
        return tripRepository.findByTripNm(tripEntity, PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public Page<TripEntity> findSearchTripMbtiPage(TripEntity tripEntity, int pageNum, int perPage){
        return tripRepository.findByMbtiaAndMbtibAndMbticAndMbtid(tripEntity.getMbtia(), tripEntity.getMbtib(),tripEntity.getMbtic(),tripEntity.getMbtid(),PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }
    
    public Page<TripEntity> findSearchTripMbtiMultiPage(List<String> mbtiaList, List<String> mbtibList, List<String> mbticList, List<String> mbtidList, int pageNum, int perPage){
        return tripRepository.findByMbtiaInAndMbtibInAndMbticInAndMbtidIn(mbtiaList, mbtibList ,mbticList, mbtidList, PageRequest.of(pageNum-1, perPage, Sort.by(Sort.Direction.ASC, "tripNm")));
    }

    public void save(TripEntity tripEntity){
        System.out.println("kjc claim-----");
        if(userServiceClient == null){
            System.out.println("kjc claim-null----");
        } else {
            System.out.println(userServiceClient.getClaim("userId"));
        }
        tripRepository.save(tripEntity);
    }
    
    public void delete(TripEntity tripEntity){
        tripRepository.delete(tripEntity);
    }
}

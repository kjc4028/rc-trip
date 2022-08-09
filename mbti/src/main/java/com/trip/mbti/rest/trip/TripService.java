package com.trip.mbti.rest.trip;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TripService {
    @Autowired
    private TripRepository tripRepository;


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

    public List<TripEntity> findByRegUserId(TripEntity tripEntiity){
        return tripRepository.findByRegUserId(tripEntiity);
    }

    public void save(TripEntity tripEntity){
        tripRepository.save(tripEntity);
    }
    
    public void delete(TripEntity tripEntity){
        tripRepository.delete(tripEntity);
    }

    public void deleteByRegUserId(String regUserId){
        tripRepository.deleteByRegUserId(regUserId);
    }
}

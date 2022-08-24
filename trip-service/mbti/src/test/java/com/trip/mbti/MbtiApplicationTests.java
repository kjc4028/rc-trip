package com.trip.mbti;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.trip.mbti.rest.trip.TripEntity;
import com.trip.mbti.rest.trip.TripService;


@SpringBootTest
class MbtiApplicationTests {
	
	@Autowired
	TripService tripService;
	
	@Test
	public void test() {
		TripEntity tripEntity = new TripEntity();
		tripEntity.setMbtia("E");
		tripEntity.setMbtib("N");
		tripEntity.setMbtic("F");
		tripEntity.setMbtid("P");
		Page<TripEntity> tripPage = tripService.findSearchTripMbtiPage(tripEntity, 1, 10);
		System.out.println(tripPage.getContent());
		assertTrue(tripEntity.getMbtia().equals(tripPage.getContent().get(0).getMbtia()));
		assertTrue(tripEntity.getMbtib().equals(tripPage.getContent().get(0).getMbtib()));
		assertTrue(tripEntity.getMbtic().equals(tripPage.getContent().get(0).getMbtic()));
		assertTrue(tripEntity.getMbtid().equals(tripPage.getContent().get(0).getMbtid()));
		
	}

	@Test
	public void selectTest(){
		List<TripEntity> list = tripService.findAllTripEntity();
		int cnt = 0;
		for(TripEntity trip : list){
			System.out.println("=====================");
			System.out.println(trip.get_Id());
			System.out.println(trip.getTripNm());
			System.out.println(trip.getTripCts());
			cnt++;
		}
		
		assertTrue("cnt 비교 ", cnt == list.size());
	}

	@Test
	public void deleteTest(){
		String testid = "6273bd24271c5e4b63b33bde";
		Optional<TripEntity> tripEntity = tripService.findOneById(testid);
		tripService.delete(tripEntity.get());
		
		if(tripService.findOneById(testid) == null){
			assertTrue(true);
		} else {
			System.out.println(tripService.findOneById(testid));

		}

		
	}
}

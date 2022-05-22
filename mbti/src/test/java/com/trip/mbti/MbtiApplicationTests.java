package com.trip.mbti;

import static org.junit.Assert.assertTrue;

import com.trip.mbti.rest.trip.TripEntity;
import com.trip.mbti.rest.trip.TripService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;


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

}

package com.trip.mbti;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mbti.batch.trip.category.CategoryDto;
import com.trip.mbti.rest.common.SearchDto;
import com.trip.mbti.rest.trip.TripEntity;
import com.trip.mbti.rest.trip.TripRequestDto;
import com.trip.mbti.rest.trip.TripService;
import com.trip.mbti.rest.trip.redis.TripRedisEntity;
import com.trip.mbti.rest.trip.redis.TripRedisRepository;

import ch.qos.logback.classic.Logger;


@SpringBootTest
@ActiveProfiles("test") 
@EnableCaching
class MbtiApplicationTests {
	private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

	static String apiServiceKey = "";

	@Autowired
	TripService tripService;

	@Autowired
    TripRedisRepository tripRedisRepository;
	
	
	@Test
	public void redisTest(){
		
		TripRedisEntity tripRedisEntity = TripRedisEntity.builder()
		.mbtia("E")
		.mbtib("N")
		.mbtic("F")
		.mbtid("P")
		.tripNm("redisTestData")
		.tripCts("redisTestCts")
		.regUserId("RedistestUser").build();


		//캐시 초기화
		tripRedisRepository.deleteAll();

		assertTrue("redis data init check", tripRedisRepository.count() == 0);

		tripRedisRepository.save(tripRedisEntity);

		assertTrue("redis data add check", tripRedisRepository.count() == 1);
		
		
	}

	@Test
	public void insertTest(){
		TripRequestDto tripRequestDto = new TripRequestDto();
		tripRequestDto.setMbtia("E");
		tripRequestDto.setMbtib("N");
		tripRequestDto.setMbtic("F");
		tripRequestDto.setMbtid("P");
		tripRequestDto.setTripNm("테스트 제목");
		tripRequestDto.setTripCts("테스트 내용");
		tripRequestDto.setRegUserId("testUser");
		

		TripEntity tripEntity = tripRequestDto.toEntity();
		tripService.save(tripEntity);

		assertTrue(true);
	}

	@Test
	public void test() {
		SearchDto searchDto = new SearchDto();
		searchDto.setSrchMbtia("E");
		searchDto.setSrchMbtib("N");
		searchDto.setSrchMbtic("F");
		searchDto.setSrchMbtid("P");
		
		TripEntity tripEntity = searchDto.toEntity();
		//TripEntity tripEntity = new TripEntity();
		// tripEntity.setMbtia("E");
		// tripEntity.setMbtib("N");
		// tripEntity.setMbtic("F");
		// tripEntity.setMbtid("P");
		Page<TripEntity> tripPage = tripService.findSearchTripMbtiPage(tripEntity, 1, 10);
		log.info("testTotalCnt : " + tripPage.getContent());
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
			log.info("=====================");
			log.info(trip.get_Id());
			log.info(trip.getTripNm());
			log.info(trip.getTripCts());
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
			log.info(""+tripService.findOneById(testid));
		}

		
	}

	//@Test
	public void extApiCall(){
		try {
            // URL 객체 생성
            URL url = new URL("https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey="+apiServiceKey+"&_type=json&cat1=C01");

            // URL을 통해 연결을 열고 데이터를 읽을 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));



            String line;
            StringBuilder content = new StringBuilder();

            // 데이터를 읽어옴
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

			String callResultJson = content.toString();
			JSONObject parsed_data = new JSONObject(callResultJson);
			JSONObject responseObj = parsed_data.getJSONObject("response");
			JSONObject bodyObj = responseObj.getJSONObject("body");
			JSONObject itemsObj = bodyObj.getJSONObject("items");
			JSONArray itemObj = itemsObj.getJSONArray("item");

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			List<CategoryDto> list = objectMapper.readValue(itemObj.toString(), new TypeReference<List<CategoryDto>>() {});
	
			for (CategoryDto categoryVo : list) {
				System.out.println("Name: " + categoryVo.getCode());			
			}

            // 읽어온 데이터 출력
            System.out.println(content.toString());

            // 리더를 닫음
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	


}

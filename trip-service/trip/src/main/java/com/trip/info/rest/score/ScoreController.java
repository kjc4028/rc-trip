package com.trip.info.rest.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    
    @GetMapping("/trips/score/get")
    @ResponseBody
    public void getScore(@RequestParam String apiKey){
        scoreService.extractClsScore("전국 각지의 농수축산물이 모이는 가락몰에서, 전국 각지의 빵 맛집들이 모여 서울 최초의 전국 빵 축제를 개최한다. 축제는 5월 9일(금)부터 5월 11일(일)까지 3일간, 가락몰 하늘공원에서 진행된다. 20개의 전국 유명 빵집이 참여하며, 총 100종 이상의 다양한 빵을 선보인다. 축제 기간 동안 버블&매직쇼, 풍선 빵 오마카세와 같은 다양한 볼거리와 가락몰 및 행사장 구매고객대상 꽝없는 빵쿠폰 뽑기와 신라호텔 망고쇼트케이크, 나폴레옹과자점 쿠키세트 추첨 이벤트도 진행된다.선착순 400명의 사전 예약자에게는 1만원 빵쿠폰도 제공된다. 사진맛집 가락몰 하늘공원에서, 향긋한 빵 향기와 함께 가족, 친구, 연인과 멋진 추억을 남길 수 있다.", apiKey);
    }
}

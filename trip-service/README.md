# mbti_trip
#### 사용자의 MBTI에 알맞는 여행지를 추천해주는 서비스

* <b>사용기술</b>
  * StirngBoot
  * MongDB
  * JWT
  * RestfulAPI

<br>

* <b>API 명세</b>

  * 회원가입<br>
  POST /usr/signup

  * 로그인<br>
  POST /usr/login

  * 여행지 목록 조회<br>
  GET /trips

  * 여행지 검색(개인)<br>
  GET /trips/searching/base

  * 여행지 검색(그룹)<br>
  GET /trips/searching/multi

  * 여행지 등록<br>
  POST /trips

  * 여행지 상세정보 조회<br>
  GET /trips/{_id}

  * 여행지 상세정보 수정<br>
  PUT /trips/{_id}

  * 여행지 상세정보 삭제<br>
  DELETE /trips/{_id}

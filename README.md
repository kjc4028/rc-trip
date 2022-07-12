# mbti_trip
#### 사용자의 MBTI에 알맞는 여행지를 추천해주는 서비스

* <b>사용기술</b>
  * StirngBoot
  * MongDB
  * JWT
  * RestfulAPI

<br>

* <b>API 명세</b>

  * 회원가입
  POST /usr/signup

  * 로그인
  POST /usr/login

  * 여행지 목록 조회
  GET /rest/trips

  * 여행지 검색(개인)
  GET /rest/trips/searching/base

  * 여행지 검색(그룹)
  GET /rest/trips/searching/multi

  * 여행지 등록
  POST /rest/trips

  * 여행지 상세정보 조회
  GET /rest/trips/{_id}

  * 여행지 상세정보 수정
  PUT /rest/trips/{_id}

  * 여행지 상세정보 삭제
  DELETE /rest/trips/{_id}

# mbti_trip_service

* ### 서비스 구성도
<img src="https://user-images.githubusercontent.com/29012197/189926018-cec4ef8a-5798-49fb-b7f5-aff316eaf12d.png" width="800"/>

---
* ### 프로젝트 설명
  * 선택한 MBTI에 맞는 여행지를 추천해주는 서비스 입니다. 해당 프로젝트는 Spting Boot 기반 MSA 환경의 프로젝트로 각 서비스 간의 통신은 openfeign을 사용하였으며 사용자 인증은 JWT 방식을 사용하였습니다.
---
* ### 프로젝트별 설명

  * #### apigateway-service
    * API Gateway의 역할
    * 요청에 따라 처리를 할 서비스로 요청 전달
    * 모든 요청은 해당 서비스에서 JWT 검증 후 동작
  * #### discovery-service
    * Service discovery 역할
    * 각 서비스의 IP와 PORT 정보 관리  
  * #### user-service
    * 회원가입, 로그인, 인증/인가 담당
    * JWT 토큰 발급
    * mongoDB와 통신
    * openfeign으로 trip-service와 통신
  * #### trip-service
    * 여행 검색 서비스 제공
    * RESTful API 구현
    * mongoDB와 통신
    * openfeign으로 user-service와 통신






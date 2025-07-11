import axios from 'axios';

// API 서버 baseURL 상수
export const BASE_URL = 'http://localhost:5555';

// axios 기본 baseURL 설정
axios.defaults.baseURL = BASE_URL;

// axios 인터셉터 설정
axios.interceptors.response.use(
  (response) => {
    // 성공적인 응답은 그대로 반환
    return response;
  },
  (error) => {
    // 998 상태 코드 토큰불일치 처리 
    if (error.response && error.response.status === 998) {
      alert("토큰정보가 없거나 불일치합니다. 다시 로그인해주시기바랍니다.");
      // localStorage에서 인증 정보 제거
      localStorage.removeItem("Authorization");
      localStorage.removeItem("userId");
      // 로그인 페이지로 리다이렉트
      window.location.href = "/login";
    } else if (error.response && error.response.status === 999) {
      alert("토큰이 만료되었습니다. 다시 로그인해주시기바랍니다.");
      // localStorage에서 인증 정보 제거
      localStorage.removeItem("Authorization");
      localStorage.removeItem("userId");
      // 로그인 페이지로 리다이렉트
      window.location.href = "/login";
    } else {
      // 지정되지 않은 오류 발생 시 오류 안내 페이지로 이동
      window.location.href = "/error";
    }
    // 다른 에러는 그대로 반환
    return Promise.reject(error);
  }
);

export default axios; 
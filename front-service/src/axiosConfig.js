import axios from 'axios';

// API 서버 baseURL 상수
export const BASE_URL = 'http://localhost:5555';

// axios 기본 baseURL 설정
axios.defaults.baseURL = BASE_URL;

// 1. Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('Authorization');
    if (token) {
      config.headers['Authorization'] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

let isRefreshing = false;
let refreshSubscribers = [];

function onRefreshed(newToken) {
  refreshSubscribers.forEach((callback) => callback(newToken));
  refreshSubscribers = [];
}

function addRefreshSubscriber(callback) {
  refreshSubscribers.push(callback);
}

// axios 인터셉터 설정
apiClient.interceptors.response.use(
  (response) => {
    // 성공적인 응답은 그대로 반환
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    // 998 상태 코드 토큰불일치 처리 
    if (error.response && error.response.status === 998) {
      alert("토큰정보가 없거나 불일치합니다. 다시 로그인해주시기바랍니다.");
      // localStorage.removeItem("Authorization");
      // localStorage.removeItem("userId");
      // window.location.href = "/login";
    } else if (error.response && error.response.status === 999) {
      alert("isRefreshing")
      if (!isRefreshing) {
        isRefreshing = true;
        try {
          const userId = localStorage.getItem("userId");
          if (!userId) throw new Error("userId 없음");
          const refreshRes = await axios.post(BASE_URL+"/user/tokenRefresh", { userId });
          const newToken = refreshRes.data;
          console.log("[토큰갱신] newToken:", newToken);
          if (typeof newToken === "string" && newToken.length > 20) { // jwt 길이 임의 체크
            localStorage.setItem("Authorization", newToken);
            console.log("[토큰갱신] localStorage.Authorization:", localStorage.getItem("Authorization"));
            onRefreshed(newToken);
            isRefreshing = false;
            // 원래 요청 헤더에 새 토큰 적용 후 재시도
            originalRequest.headers["Authorization"] = newToken;
            return apiClient(originalRequest);
          } else {
            throw new Error("refresh 실패");
          }
        } catch (e) {
          isRefreshing = false;
          onRefreshed(null);
          alert("토큰이 만료되었습니다. 다시 로그인해주시기바랍니다.");
          localStorage.removeItem("Authorization");
          localStorage.removeItem("userId");
          window.location.href = "/login";
          return;
        }
      } else {
        // 이미 refresh 중이면, 새 토큰 발급 후 재시도 대기
        return new Promise((resolve, reject) => {
          addRefreshSubscriber((newToken) => {
            if (newToken) {
              originalRequest.headers["Authorization"] = newToken;
              resolve(apiClient(originalRequest));
            } else {
              reject(error);
            }
          });
        });
      }
    } 
    // return Promise.reject(error);
  }
);

export default apiClient; 
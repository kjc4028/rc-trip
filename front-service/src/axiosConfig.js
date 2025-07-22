import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:5555', // gateway 주소
  withCredentials: true
});

// 토큰 저장/불러오기 함수 (localStorage 예시)
function getAccessToken() {
  return localStorage.getItem('Authorization');
}
function setAccessToken(token) {
  localStorage.setItem('Authorization', token);
}

// 요청 인터셉터: access token 자동 첨부
api.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers['Authorization'] = `${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: access token 만료 시 refresh 시도
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    // 999: 토큰 만료(커스텀)
    if (error.response && error.response.status === 999 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = getAccessToken();
        const res = await axios.post(
          'http://localhost:5555/user/tokenRefresh',
          {userId: localStorage.getItem('userId')},{
            headers: { 'Authorization': `${refreshToken}`},
            withCredentials: true,
          }
        );
        const newAccessToken = res.headers.authorization;
        setAccessToken(newAccessToken);
        // 원래 요청에 새 토큰 적용 후 재시도
        originalRequest.headers['Authorization'] = `${newAccessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // refresh 실패 시 로그아웃 등 처리
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    } else if (error.response && error.response.status === 998) {
      // localStorage에서 인증 정보 제거
      localStorage.removeItem("Authorization");
      localStorage.removeItem("userId");
      // 인증 안내 페이지로 이동
      window.location.href = "/auth-required";
    } else {
      window.location.href = "/error";
    }
    // return Promise.reject(error);
  }
);

export default api;
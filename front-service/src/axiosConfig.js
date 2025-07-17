import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:55555', // gateway 주소
  withCredentials: true,
});

// 토큰 저장/불러오기 함수 (localStorage 예시)
function getAccessToken() {
  return localStorage.getItem('Authorization');
}
function getRefreshToken() {
  return localStorage.getItem('refreshToken');
}
function setAccessToken(token) {
  localStorage.setItem('accessToken', token);
}

// 요청 인터셉터: access token 자동 첨부
api.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
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
        // refresh token으로 access token 재발급 요청
        // const refreshToken = getRefreshToken();
        const refreshToken = getAccessToken();
        const res = await axios.post(
          'http://localhost:5555/user/tokenRefresh',
          {},
          {
            headers: { 'Authorization': `Bearer ${refreshToken}` },
            withCredentials: true,
          }
        );
        const newAccessToken = res.data.accessToken;
        setAccessToken(newAccessToken);
        // 원래 요청에 새 토큰 적용 후 재시도
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // refresh 실패 시 로그아웃 등 처리
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    } else if (error.response && error.response.status === 998) {
      alert("토큰정보가 없거나 불일치합니다. 다시 로그인해주시기바랍니다.");
      // localStorage에서 인증 정보 제거
      localStorage.removeItem("Authorization");
      localStorage.removeItem("userId");
      // 로그인 페이지로 리다이렉트
      window.location.href = "/login";
    } else {
      alert("알 수 없는 오류 발생하였습니다.");
      // localStorage에서 인증 정보 제거
      localStorage.removeItem("Authorization");
      localStorage.removeItem("userId");
      // 로그인 페이지로 리다이렉트
      window.location.href = "/login";
    }
    // return Promise.reject(error);
  }
);

export default api;
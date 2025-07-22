import React from 'react';
import { useNavigate } from 'react-router-dom';

function AuthRequired() {
  const navigate = useNavigate();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh' }}>
      <h2>로그인이 필요합니다</h2>
      <p>이 페이지는 로그인 후 이용하실 수 있습니다.<br/>로그인 후 다시 시도해 주세요.</p>
      <div style={{ marginTop: 24 }}>
        <button onClick={() => navigate('/login')} style={{ marginRight: 12, padding: '10px 20px', borderRadius: 5, background: '#1976d2', color: '#fff', border: 'none', fontSize: '1.1rem', cursor: 'pointer' }}>
          로그인
        </button>
        <button onClick={() => navigate('/')} style={{ padding: '10px 20px', borderRadius: 5, background: '#e0eafc', color: '#1976d2', border: 'none', fontSize: '1.1rem', cursor: 'pointer' }}>
          홈으로 이동
        </button>
      </div>
    </div>
  );
}

export default AuthRequired; 
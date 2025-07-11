import React from 'react';
import { useNavigate } from 'react-router-dom';

function ErrorPage() {
  const navigate = useNavigate();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh' }}>
      <h2>오류가 발생했습니다</h2>
      <p>예기치 않은 오류가 발생했습니다. 잠시 후 다시 시도해주시기 바랍니다.</p>
      <button onClick={() => navigate('/')} style={{ marginTop: 20, padding: '10px 20px', borderRadius: 5, background: '#1976d2', color: '#fff', border: 'none', fontSize: '1.1rem', cursor: 'pointer' }}>
        홈으로 이동
      </button>
    </div>
  );
}

export default ErrorPage; 
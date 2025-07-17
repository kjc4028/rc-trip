import React, { useState } from "react";
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import api from './axiosConfig';
import TripDtl from './TripDtl';

function TalkSearch() {
  const [sentence, setSentence] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [mode, setMode] = useState('search'); // 'search', 'dtl'
  const [tripDtl, setTripDtl] = useState(null);
  const [pageAble, setPageAble] = useState(null);

  const handleInputChange = (e) => {
    setSentence(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setResults([]);
    try {
      const response = await api.post(
        `/search/talk`,
        { sentence }
      );
      setResults(response.data.data);
    } catch (err) {
      setError("검색 결과를 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // TripStyleSelector의 goDtl과 유사한 함수
  const goDtl = async (tripId) => {
    try {
      let response = await api.get(`/trips/${tripId}`);
      setTripDtl(response.data.data);
      setMode('dtl');
      setPageAble(null); // 필요시 전달
    } catch (error) {
      alert('상세 정보를 불러오지 못했습니다.');
    }
  };

  // TripDtl에서 목록으로 돌아가기
  const handleBackToList = () => {
    setMode('search');
  };

  const renderCards = (trips) => (
    <Row xs={1} md={1} className="g-4" style={{ justifyContent: "center" }}>
      {(Array.isArray(trips) ? trips : []).map((trip, i) => (
        <Col key={trip.contentId || i} style={{ display: "flex", justifyContent: "center" }}>
          <Card style={{ minWidth: "320px", maxWidth: "480px", width: "100%", cursor: 'pointer' }}
            onClick={() => goDtl(trip.contentId)}>
            <Card.Body>
              <Card.Title>{trip.tripNm}</Card.Title>
              <Card.Text>{trip.tripCts}</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      ))}
    </Row>
  );

  // 렌더링 분기
  if (mode === 'dtl' && tripDtl) {
    return <TripDtl tripDtl={tripDtl} pageAble={pageAble} onBack={handleBackToList} />;
  }

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", minHeight: "60vh" }}>
      <h3>문장으로 여행지 추천받기</h3>
      <form onSubmit={handleSubmit} style={{ margin: "20px 0", width: "100%", maxWidth: "480px" }}>
        <input
          type="text"
          name="sentence"
          value={sentence}
          onChange={handleInputChange}
          placeholder="여행 스타일이나 원하는 경험을 입력하세요."
          style={{ width: "100%", padding: "12px", fontSize: "1.1rem", borderRadius: "5px", border: "1px solid #ccc" }}
        />
        <button
          type="submit"
          style={{ marginTop: "16px", width: "100%", padding: "10px", background: "#1976d2", color: "#fff", border: "none", borderRadius: "5px", fontSize: "1.1rem", cursor: "pointer" }}
          disabled={loading || !sentence.trim()}
        >
          {loading ? "검색 중..." : "여행지 추천받기"}
        </button>
      </form>
      {error && <div style={{ color: "red", marginTop: "10px" }}>{error}</div>}
      {results && results.length > 0 && (
        <div style={{ width: "100%", marginTop: "40px", justifyContent: "center" }}>
          <h4 style={{ textAlign: "center" }}>추천 여행지 목록</h4>
          {renderCards(results)}
        </div>
      )}
    </div>
  );
}

export default TalkSearch; 
import React, { useState } from "react";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import CardGroup from 'react-bootstrap/CardGroup';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Pagination from 'react-bootstrap/Pagination';
import axios from "axios";
import TripDtl from './TripDtl';

const styles = [
  { code: 1, label: "힐링" },
  { code: 2, label: "액티비티" },
  { code: 3, label: "계획" },
  { code: 4, label: "즉흥" },
  { code: 5, label: "관광" },
  { code: 6, label: "로컬" },
  { code: 7, label: "미식" },
  { code: 8, label: "쇼핑" },
];

function TripStyleSelector({ onSelect }) {
  const [selectedItems, setSelectedItems] = useState([]);
  const [recommendedTrips, setRecommendedTrips] = useState([]);
  const [mode, setMode] = useState('select'); // 'select', 'list', 'dtl'
  const [tripDtl, setTripDtl] = useState(null);
  const [pageAble, setPageAble] = useState(null); // 필요시 사용

  const handleSelect = (code) => {
    if (selectedItems.includes(code)) {
      setSelectedItems(selectedItems.filter((item) => item !== code));
    } else {
      if (selectedItems.length < 3) {
        setSelectedItems([...selectedItems, code]);
      }
    }
  };

  // 3개 선택 시만 onSelect 호출
  React.useEffect(() => {
    if (selectedItems.length === 3 && typeof onSelect === "function") {
      onSelect(selectedItems);
    }
  }, [selectedItems, onSelect]);

  const handleCheckTrips = async () => {
    if (selectedItems.length !== 3) return;
    try {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        const response = await axios.get(
        `http://localhost:5555/trips/trip-smr/top3`,
        {
          params: { selectedItems: selectedItems.join(",") }
        }
      );
      setRecommendedTrips(response.data.data);
    } catch (error) {
      alert("여행지 추천 결과를 불러오지 못했습니다.");
    }
  };

  // TripList의 goDtl과 유사한 함수
  const goDtl = async (tripId) => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    try {
      let response = await axios.get('http://localhost:5555/trips/' + tripId);
      setTripDtl(response.data.data);
      setMode('dtl');
      setPageAble(null); // 필요시 전달
    } catch (error) {
      alert('상세 정보를 불러오지 못했습니다.');
    }
  };

  // TripDtl에서 목록으로 돌아가기
  const handleBackToList = () => {
    setMode('top3');
  };

  // TripList 스타일 카드 렌더링 함수
  const renderTripCards = (trips) => (
    <Row xs={1} md={1} className="g-4" style={{ justifyContent: "center" }}>
      {(Array.isArray(trips) ? trips : []).map((trip, i) => (
        <Col key={trip._Id || i} style={{ display: "flex", justifyContent: "center" }}>
          <Card style={{ minWidth: "320px", maxWidth: "480px", width: "100%", cursor: 'pointer' }}
            onClick={() => goDtl(trip._Id)}>
            {/* <Card.Img variant="top" src="holder.js/100px160" /> */}
            <Card.Body>
              <Card.Title>{trip.tripNm}</Card.Title>
              <Card.Text>
                {trip.tripCts}
              </Card.Text>
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
      <h3>여행 스타일을 3가지 선택하세요</h3>
      <div style={{
        display: "grid",
        gridTemplateColumns: "1fr 1fr",
        gap: "16px",
        margin: "20px 0"
      }}>
        {styles.map((style) => (
          <button
            key={style.code}
            onClick={() => handleSelect(style.code)}
            style={{
              background: selectedItems.includes(style.code) ? "#4caf50" : "#eee",
              color: selectedItems.includes(style.code) ? "#fff" : "#000",
              padding: "20px 0",
              border: "none",
              borderRadius: "5px",
              cursor: "pointer",
              fontSize: "1.1rem",
              opacity: selectedItems.length === 3 && !selectedItems.includes(style.code) ? 0.5 : 1,
              width: "160px"
            }}
            disabled={
              selectedItems.length === 3 && !selectedItems.includes(style.code)
            }
          >
            {style.label}
          </button>
        ))}
      </div>
      <div style={{ marginTop: "20px", color: "red" }}>
        {selectedItems.length < 3 && "3가지를 꼭 선택해야 합니다."}
        {selectedItems.length > 3 && "3가지만 선택할 수 있습니다."}
      </div>
      <button
        style={{
          marginTop: "20px",
          padding: "10px 20px",
          background: "#1976d2",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: selectedItems.length === 3 ? "pointer" : "not-allowed",
          opacity: selectedItems.length === 3 ? 1 : 0.5
        }}
        disabled={selectedItems.length !== 3}
        onClick={() => { setMode('top3'); handleCheckTrips(); }}
      >
        여행지 확인하기
      </button>
      {/* 추천 결과가 있을 때 TripList 스타일로 표시 */}
      {mode === 'top3' && recommendedTrips && recommendedTrips.length > 0 && (
        <div style={{ width: "100%", marginTop: "40px", justifyContent: "center"}}>
          <h4 style={{ textAlign: "center" }}>추천 여행지 목록</h4>
          {renderTripCards(recommendedTrips)}
        </div>
      )}
    </div>
  );
}

export default TripStyleSelector;
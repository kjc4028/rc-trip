import React, { useState } from "react";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import CardGroup from 'react-bootstrap/CardGroup';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Pagination from 'react-bootstrap/Pagination';
import axios from "axios";

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
      alert("여행지 추천 결과: " + JSON.stringify(response.data));
    } catch (error) {
      alert("여행지 추천 결과를 불러오지 못했습니다.");
    }
  };

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
        onClick={handleCheckTrips}
      >
        여행지 확인하기
      </button>
    </div>
  );
}

export default TripStyleSelector;
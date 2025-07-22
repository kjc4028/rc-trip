
import api from './axiosConfig';
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Row, Col, Form, Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";
import './App.css';
//가입결과
function JoinRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

//가입
function Join() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  function joinBtn(e){
    e.preventDefault();
    setLoading(true);
    setError(null);
    api.post(`/user/signup`,{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      setData(res.data.message);
      setLoading(false);
      if(res.data.data == "joinSucc"){
        setTimeout(() => navigate("/login"), 1500);
      }
    })
    .catch((err) => {
      setLoading(false);
      setError("회원가입에 실패했습니다. 다시 시도해주세요.");
    });
  }

  return (
    <div className="auth-container">
      <Container className="auth-panel">
        <h2 className="auth-title">회원가입</h2>
        <Form onSubmit={joinBtn}>
          <Form.Group className="mb-3" controlId="userId">
            <Form.Control type="text" placeholder="아이디" name="userId" autoFocus required />
          </Form.Group>
          <Form.Group className="mb-3" controlId="userPw">
            <Form.Control type="password" placeholder="비밀번호" name="userPw" required />
          </Form.Group>
          {error && <div className="auth-error">{error}</div>}
          <Button variant="primary" type="submit" disabled={loading} className="w-100 mb-2">
            {loading ? "가입 중..." : "회원가입"}
          </Button>
        </Form>
        <div className="auth-link">
          <span>이미 계정이 있으신가요? </span>
          <Link to="/login">로그인</Link>
        </div>
        <JoinRs rsdata={data}></JoinRs>
      </Container>
    </div>
  );
}

export default Join;

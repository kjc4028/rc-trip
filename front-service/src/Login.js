import { type } from "@testing-library/user-event/dist/type";
import api from './axiosConfig';
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import Join from "./Join";
import TripList from "./TripList";
import { Row, Col, Form, Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";
import './App.css';
//가입결과
function LoginRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

function JoinRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

function Login() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  function loginBtn(e){
    e.preventDefault();
    setLoading(true);
    setError(null);
    api.post(`/user/login`,{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      setData(res.data);
      let jwtToken = res.headers.authorization;
      let userId = res.data.data.userId;
      localStorage.setItem("Authorization", jwtToken);
      localStorage.setItem("userId", userId);
      setLoading(false);
      navigate("/style");
    })
    .catch((err) => {
      setLoading(false);
      setError("로그인에 실패했습니다. 아이디와 비밀번호를 확인하세요.");
    });
  }

  return (
    <div className="auth-container">
      <Container className="auth-panel">
        <h2 className="auth-title">로그인</h2>
        <Form onSubmit={loginBtn}>
          <Form.Group className="mb-3" controlId="userId">
            <Form.Control type="text" placeholder="아이디" name="userId" autoFocus required />
          </Form.Group>
          <Form.Group className="mb-3" controlId="userPw">
            <Form.Control type="password" placeholder="비밀번호" name="userPw" required />
          </Form.Group>
          {error && <div className="auth-error">{error}</div>}
          <Button variant="primary" type="submit" disabled={loading} className="w-100 mb-2">
            {loading ? "로그인 중..." : "로그인"}
          </Button>
        </Form>
        <div className="auth-link">
          <span>계정이 없으신가요? </span>
          <Link to="/join">회원가입</Link>
        </div>
        <LoginRs rsdata={data}></LoginRs>
      </Container>
    </div>
  );
}

export default Login;

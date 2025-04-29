import axios from "axios";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Login from "./Login";
import { Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";

function Header() {
    const navigate = useNavigate();
    const [logoutBtn, setLogoutBtn] = useState(null);
    const logoutBtnConst = "";
    // if(localStorage.getItem("Authorization") !== null){
    //     logoutBtnConst = <button>로그아웃</button>;
    // }
    //<button onClick={logout}>로그아웃</button>
    function logout(){
        localStorage.removeItem("Authorization");
        alert("로그아웃 되었습니다.");
        window.location.replace("/");
    }

    function login(){
    }

    return (
      <div className="Header">
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand as={Link} to="/">MBTI TRIP</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to="/">Home</Nav.Link>
              <Nav.Link as={Link} to="trip-list">전체여행목록</Nav.Link>
              <Nav.Link as={Link} to="trip-srch-base">혼자 여행가기</Nav.Link>
              <Nav.Link as={Link} to="trip-srch-multi">다같이 여행가기</Nav.Link>
              <Nav.Link as={Link} to="admin">관리자 메뉴</Nav.Link>
              <Nav.Link as={Link} to="admin/trips">여행지 관리</Nav.Link>
              <Nav.Link onClick={logout}>로그아웃</Nav.Link>
              <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                <NavDropdown.Item href="#action/3.2">
                  Another action
                </NavDropdown.Item>
                <NavDropdown.Item href="#action/3.3">
                  Something
                </NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item href="#action/3.4">
                  Separated link
                </NavDropdown.Item>
              </NavDropdown>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>              
          {/* <button onClick={logout}>로그아웃</button><span> | </span>
          <Link to="trip-list">tripList</Link><span> | </span>
          <Link to="trip-srch-multi">tripSrchMult</Link><span> | </span> */}
        </div>
        )
        
}

export default Header;

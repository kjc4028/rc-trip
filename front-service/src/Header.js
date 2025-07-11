import axios from "axios";
import { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Login from "./Login";
import { Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";

function Header() {
    const navigate = useNavigate();
    const [logoutBtn, setLogoutBtn] = useState(null);
    const [expanded, setExpanded] = useState(false);
    const [userId, setUserId] = useState(null);
    const navbarRef = useRef(null);
    const logoutBtnConst = "";

    useEffect(() => {
        const storedUserId = localStorage.getItem("userId");
        setUserId(storedUserId);
    }, []);

    function logout(){
        localStorage.removeItem("Authorization");
        localStorage.removeItem("userId");
        setUserId(null);
        alert("로그아웃 되었습니다.");
        window.location.replace("/");
    }

    function login(){
        navigate("/login");
    }

    // 마우스가 네비게이션 바 영역을 벗어날 때 메뉴 접기
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (navbarRef.current && !navbarRef.current.contains(event.target)) {
                setExpanded(false);
            }
        };

        const handleMouseLeave = () => {
            setExpanded(false);
        };

        if (expanded) {
            document.addEventListener('mousedown', handleClickOutside);
            if (navbarRef.current) {
                navbarRef.current.addEventListener('mouseleave', handleMouseLeave);
            }
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
            if (navbarRef.current) {
                navbarRef.current.removeEventListener('mouseleave', handleMouseLeave);
            }
        };
    }, [expanded]);

    // navbar-toggler에 마우스 올렸을 때 메뉴 펼치기
    useEffect(() => {
        const toggler = document.querySelector('.navbar-toggler');
        if (toggler) {
            const handleMouseEnter = () => {
                setExpanded(true);
            };

            toggler.addEventListener('mouseenter', handleMouseEnter);

            return () => {
                toggler.removeEventListener('mouseenter', handleMouseEnter);
            };
        }
    }, []);

    return (
      <div className="Header">
      <Navbar bg="light" expand="lg" expanded={expanded} onToggle={(expanded) => setExpanded(expanded)} ref={navbarRef}>
        <Container>
          <Navbar.Brand as={Link} to="/" onClick={() => setExpanded(false)}>MBTI TRIP</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to="/" onClick={() => setExpanded(false)}>Home</Nav.Link>
              <Nav.Link as={Link} to="trip-list" onClick={() => setExpanded(false)}>전체여행목록</Nav.Link>
              <Nav.Link as={Link} to="style" onClick={() => setExpanded(false)}>여행스타일추천</Nav.Link>
              <Nav.Link as={Link} to="talk" onClick={() => setExpanded(false)}>톡추천</Nav.Link>
              {/* <Nav.Link as={Link} to="trip-srch-base">혼자 여행가기</Nav.Link>
              <Nav.Link as={Link} to="trip-srch-multi">다같이 여행가기</Nav.Link> */}
              <Nav.Link as={Link} to="admin" onClick={() => setExpanded(false)}>관리자 메뉴</Nav.Link>
              <Nav.Link as={Link} to="admin/trips" onClick={() => setExpanded(false)}>여행지 관리</Nav.Link>
              {userId ? (
                <>
                  <Nav.Link style={{ color: '#007bff', fontWeight: 'bold' }}>{userId}</Nav.Link>
                  <Nav.Link onClick={() => { logout(); setExpanded(false); }}>로그아웃</Nav.Link>
                </>
              ) : (
                <Nav.Link onClick={() => { login(); setExpanded(false); }}>로그인</Nav.Link>
              )}
              </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>              

        </div>
        )
        
}

export default Header;

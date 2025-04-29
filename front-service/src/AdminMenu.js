import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { Nav, Navbar, Container } from 'react-bootstrap';
import TripManagement from './TripManagement';

const AdminMenu = () => {
  return (
    <>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          <Navbar.Brand as={Link} to="admin/">관리자 메뉴</Navbar.Brand>
          <Navbar.Toggle aria-controls="admin-navbar" />
          <Navbar.Collapse id="admin-navbar">
            <Nav className="me-auto">
                <Nav.Link as={Link} to="trips">여행지 관리</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <Routes>
          <Route path="/" element={<div className="mt-4"><h2>관리자 메인 페이지</h2></div>} />
          <Route path="admin/trips" element={<TripManagement />} />
        </Routes>
      </Container>
    </>
  );
};

export default AdminMenu;
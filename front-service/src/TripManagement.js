import React, { useState, useEffect } from 'react';
import { Table, Button, Container, Modal, Form } from 'react-bootstrap';
import api from './axiosConfig';

const TripManagement = () => {
  const [trips, setTrips] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [apiKey, setApiKey] = useState('');

  useEffect(() => {
    // TODO: API 연동
    // api.get('/api/admin/trips')
    //   .then(response => setTrips(response.data))
    //   .catch(error => console.error('Error fetching trips:', error));
  }, []);

  const handleRunBatch = () => {
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
    setApiKey('');
  };

  const handleSubmit = async () => {
    try {
      await api.get(`/trips/batch/all?apiKey=${apiKey}`);
      handleClose();
    } catch (error) {
      console.error('Error running batch:', error);
      alert('배치 실행 중 오류가 발생했습니다.');
    }
  };

  return (
    <Container className="mt-4">
      <h2>여행지 관리</h2>
      <Button variant="success" className="mb-3" onClick={handleRunBatch}>API 수집 배치 실행</Button>

      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>API 키 입력</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>API 키</Form.Label>
              <Form.Control
                type="password"
                value={apiKey}
                onChange={(e) => setApiKey(e.target.value)}
                placeholder="API 키를 입력하세요"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            취소
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            실행
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default TripManagement; 
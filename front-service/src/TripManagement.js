import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Container, Modal, Form, Col, Row } from 'react-bootstrap';
import api from './axiosConfig';

const TripManagement = () => {
  const [trips, setTrips] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [apiKey, setApiKey] = useState('');
  const [workJobName, setWorkJobName] = useState('');



  const navigate = useNavigate();
  useEffect(() => {
    const token = localStorage.getItem('Authorization');
    if (!token) {
      navigate('/auth-required');
    }
  }, [navigate]);

  const handleRunBatch = (jobname, apiNeedYn) => {
    setWorkJobName(jobname);
    if(apiNeedYn === 'Y') {
      setShowModal(true);
      return;
    }

    handleSubmit();

  };

  const handleClose = () => {
    setShowModal(false);
    setApiKey('');
    setWorkJobName('');
  };

  const handleSubmit = async () => {
    
    try {
      await api.get(`/trips/batch/${workJobName}?apiKey=${apiKey}`);
      handleClose();
    } catch (error) {
      console.error('Error running batch:', error);
      alert('배치 실행 중 오류가 발생했습니다.');
    }
  };

const serviceCall = async (serviceUrl) => {
    
    try {
      await api.post(serviceUrl);
      handleClose();
    } catch (error) {
      console.error('Error running batch:', error);
      alert('서비스 호출 실행 중 오류가 발생했습니다.');
    }
  };

  return (
    <Container className="mt-4">
      <h2>관광지 관리</h2>

            <Row>
                <Col xs={12} className="mb-2">
                    <h5 className="fw-bold">1. 관광정보 수집</h5>
                </Col>
            </Row>
            <Row>
                <Col xs={6} className="mb-3">
                    <Button variant="primary" className="w-100" onClick={() => handleRunBatch('jobtripinfo', 'Y')}>
                      기본정보 API 호출
                    </Button>
                </Col>          
                <Col xs={6} className="mb-3">
                    <Button variant="primary" className="w-100" onClick={() => handleRunBatch('jobtripdtlinfo', 'Y')}>
                      소개문구 API 호출
                    </Button>
                </Col>                 
            </Row>

            <Row>
                <Col xs={12} className="mb-2">
                    <h5 className="fw-bold">2. 관광정보 취향 분류 점수 산정</h5>
                </Col>
            </Row>
            <Row>
                <Col xs={6} className="mb-3">
                    <Button variant="primary" className="w-100" onClick={() => handleRunBatch('jobtripsscore', 'Y')}>
                      API 호출 배치 실행
                    </Button>
                </Col>                  
                <Col xs={6} className="mb-3">
                    <Button variant="primary" className="w-100" onClick={serviceCall.bind(null, '/trips/aggregation')}>
                      top3 요약정보 생성
                    </Button>
                </Col>            
            </Row>
      
            <Row>
                <Col xs={12} className="mb-2">
                    <h5 className="fw-bold">3. 관광소개 임베딩 처리</h5>
                </Col>
            </Row>
            <Row>
                <Col xs={12} className="mb-3">
                    <Button variant="primary" className="w-100" onClick={serviceCall.bind(null, '/search/index')}>
                      임베딩(갱신) 및 인덱싱
                    </Button>
                </Col>
                {/* <Col xs={6} className="mb-3">
                    <Button variant="secondary" className="w-100">
                      파일 데이터 일괄 적재
                    </Button>
                </Col>             */}
            </Row>

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
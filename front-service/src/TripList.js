import api from './axiosConfig';
import React, { useState, useEffect, useRef, useCallback } from "react";
import TripDtl from "./TripDtl";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import CardGroup from 'react-bootstrap/CardGroup';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Pagination from 'react-bootstrap/Pagination';

function TripList(props) {
    const [tripList, setTripList] = useState([]);
    const [mode, setMode] = useState(null);
    const [tripId, setTripId] = useState(null);
    const [tripDtl, setTripDtl] = useState();
    const [pageAble, setPageAble] = useState();
    const [pageNum, setPageNum] = useState(props.pageNum || 1);
    const [perPage] = useState(props.perPage || 10);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);
    const observer = useRef();
    const loaderRef = useRef();

    useEffect(() => {
        setTripList([]);
        setPageNum(props.pageNum || 1);
        setHasMore(true);
    }, [props.pageNum, props.perPage]);

    useEffect(() => {
        getTrips(pageNum, perPage);
        // eslint-disable-next-line
    }, [pageNum]);

    const getTrips = async (_pageNum, _perPage) => {
        if (loading || !hasMore) return;
        setLoading(true);
        api.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        const params = { pageNum: _pageNum, perPage: _perPage };
        try {
            let response = await api.get(`/trips`, { params });
            const newTrips = response.data.data.content;
            setTripList(prev => [...prev, ...newTrips]);
            setMode("list");
            if (newTrips.length < _perPage) setHasMore(false);
        } catch (error) {
            setHasMore(false);
        }
        setLoading(false);
    };

    const goDtl = async (tripId) => {
        api.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        let response = await api.get(`/trips/${tripId}`);
        setTripDtl(response.data.data);
        setTripId(tripId);
        setPageAble({ pageNum: pageNum, perPage: perPage });
        setMode("dtl");
    };

    // IntersectionObserver로 무한 스크롤 구현
    const lastTripElementRef = useCallback(node => {
        if (loading) return;
        if (observer.current) observer.current.disconnect();
        observer.current = new window.IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                setPageNum(prev => prev + 1);
            }
        });
        if (node) observer.current.observe(node);
    }, [loading, hasMore]);

    if (mode === "dtl") {
        return (<TripDtl tripDtl={tripDtl} pageAble={pageAble}></TripDtl>);
    } else {
        return (
            <>
                <Row xs={1} md={2} className="g-4">
                    {tripList && tripList.map((trip, i) => (
                        <Col key={trip.contentId} ref={i === tripList.length - 1 ? lastTripElementRef : null}>
                            <Card style={{ height: '320px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                                <Card.Body style={{ flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                                    <Card.Title onClick={() => { goDtl(trip.contentId); return false; }} style={{
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis',
                                        whiteSpace: 'nowrap',
                                        fontWeight: 'bold',
                                        fontSize: '1.2rem',
                                        marginBottom: '0.5rem',
                                    }}>{trip.tripNm}</Card.Title>
                                    <Card.Text onClick={() => { goDtl(trip.contentId); return false; }} style={{
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis',
                                        display: '-webkit-box',
                                        WebkitLineClamp: 4,
                                        WebkitBoxOrient: 'vertical',
                                        whiteSpace: 'normal',
                                        fontSize: '1rem',
                                        color: '#333',
                                        minHeight: '72px',
                                        maxHeight: '96px',
                                    }}>
                                        {trip.tripCts}
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
                {loading && (
                    <div className="text-center my-4">
                        <span>로딩중...</span>
                    </div>
                )}
                {!hasMore && tripList.length > 0 && (
                    <div className="text-center my-4">
                        <span>모든 여행지를 불러왔습니다.</span>
                    </div>
                )}
            </>
        );
    }
}

export default TripList;
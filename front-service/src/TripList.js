import axios, { BASE_URL } from './axiosConfig';
import React, { useState, useEffect } from "react";
import TripDtl from "./TripDtl";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import CardGroup from 'react-bootstrap/CardGroup';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Pagination from 'react-bootstrap/Pagination';

function TripList(props) {
    
    const [tripList, setTripList] = useState();
    const [mode, setMode] = useState(null);
    const [tripId, setTripId] = useState(null);
    const [tripDtl, setTripDtl] = useState();
    const [pageAble, setPageAble] = useState();
    const [moreListCnt, setMoreListCnt] = useState();

    const pageNum = props.pageNum;
    const perPage = props.perPage;


    useEffect(() => {
        getTrips()
        }, []);
    
    const getTrips = async () => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    const params = { pageNum:pageNum, perPage:perPage}
    let response = await axios.get(`${BASE_URL}/trips`,{params});
    console.log(response);
    setTripList(response.data.data.content);
    setMode("list");
    }          
    
    const getTripsPage = async (_pageNum, _perPage) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        const params = { pageNum:_pageNum, perPage:_perPage}
        let response = await axios.get(`${BASE_URL}/trips`,{params});
        console.log(response);
        setTripList(response.data.data.content);
        setMode("list");
        } 
          
    const goDtl = async (tripId) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
       // axios.get('http://localhost:5555/trips/'+tripId,{}, {responseType:'json', headers:{"Content-Type": "application/json"}})
        let response = await axios.get(`${BASE_URL}/trips/${tripId}`);
        console.log(response);
        setTripDtl(response.data.data);
        console.log(response.data.data);
        setTripId(tripId);
        setPageAble({pageNum:pageNum, perPage:perPage});
        setMode("dtl");
    }   

    let active = 2;
    let items = [];
    for (let number = 1; number <= 3; number++) {
    items.push(
        <Pagination.Item key={number} active={number === active} onClick={() => {getTripsPage(number)}}>
        {number}
        </Pagination.Item>,
    );
    }

    const paginationBasic = (
    <div>
        <Pagination>{items}</Pagination>
    </div>
    );

    function paging(totalPageNum){
        let pageArr = [];
        for (let index = 1; index <= totalPageNum; index++) {
             pageArr.push(<span kye={index} onClick={() => {getTripsPage(index)}}>{index}</span>);
            
        }
        return pageArr;
    }
    const moreList = async (_moreListCnt) => {
      axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
      console.log("moreList _moreListCnt: " + _moreListCnt);
      if(_moreListCnt === undefined){
        _moreListCnt = 1;
      }
      let morePageNum = _moreListCnt+1;
      console.log("moreList morePageNum: " + morePageNum);
      console.log("moreList _moreListCnt: " + _moreListCnt);
      const params = { pageNum:_moreListCnt};
      let response = "";
      try {
        response = await axios.get(`${BASE_URL}/trips`,{params});  
      } catch (error) {
        console.log("error : " + error);
        if(error.response){
          console.log("error.response.status : " + error.response.status);  
          if(error.response.status == "999"){
            // 토큰 갱신 시도
            try {
              await axios.post(`${BASE_URL}/user/tokenRefresh`,{userId : localStorage.getItem("userId")}).then((res) => {
                console.log(res);
                console.log("res header " + res.headers.authorization);
                console.log("res data " + res.data);
                let jwtToken = res.headers.authorization;
                localStorage.setItem("Authorization", jwtToken);
              });  
              response = await axios.get(`${BASE_URL}/trips`,{params});  
            } catch (refreshError) {
              // 토큰 갱신 실패 시
              alert("세션이 만료되었습니다. 다시 로그인해주세요.");
              localStorage.removeItem("Authorization");
              localStorage.removeItem("userId");
              window.location.href = "/login"; // 로그인 페이지로 리다이렉트
              return;
            }
          }
        }
      }
      
      console.log("response " + response.status)
      console.log("moreList be: " + response.data.data);
      console.log("moreList af: " + response.data.data.content);
      let originList = tripList;
      let addList = response.data.data.content;
      console.log("moreList originList : " + originList);
      addList.forEach(element => {
        originList.push(element);
      });
      console.log("moreList list merge: " + originList);
      setTripList(originList);
      setMode("addList"+morePageNum);
      setMoreListCnt(morePageNum)
    }
    // function moreList(_pageNum, _perPage){
    //   axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    //   const params = { pageNum:_pageNum, perPage:_perPage}
    //   let response = axios.get('http://localhost:5555/trips',{params});
    //   console.log("moreList : " + response);
    //   const list = tripList.content;
    //   list.push(response.data.data);
    //   setTripList(list);
    //   setMode("list");
    // }     

    if(mode === "dtl"){
        return (<TripDtl tripDtl={tripDtl} pageAble={pageAble}></TripDtl>);
    } else {
    //if(mode === "list"){
    
        return(
            <>          
     <Row xs={1} md={2} className="g-4">
      {tripList && tripList.map((trip,i) => (
        <Col>
          <Card key ={trip.contentId} style={{ height: '320px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
            {/* <Card.Img variant="top" src="holder.js/100px160" /> */}
            <Card.Body style={{ flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
              <Card.Title onClick={() => {goDtl(trip.contentId); return false;} } style={{
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
                fontWeight: 'bold',
                fontSize: '1.2rem',
                marginBottom: '0.5rem',
              }}>{trip.tripNm}</Card.Title>
              <Card.Text onClick={() => {goDtl(trip.contentId); return false;} } style={{
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
    {(!tripList || tripList.length === 0) && (
      <div className="text-center my-5">
        <p>데이터가 없습니다.</p>
      </div>
    )}
    <div className="text-center my-4">
      <button className="btn btn-outline-dark flex-shrink-0" type="button" onClick={ () => {moreList(moreListCnt);}}>
        <i className="bi-cart-fill me-1"></i>
        목록 더 불러오기
      </button>
    </div>
    </>                         

        );

    }
}

export default TripList;
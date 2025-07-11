import axios, { BASE_URL } from './axiosConfig';
import React, { useState, useEffect } from "react";
import TripDtl from "./TripDtl";
function TripSrchMulti(props) {
    
    const [tripList, setTripList] = useState();
    const [mode, setMode] = useState(null);
    const [tripId, setTripId] = useState(null);
    const [tripDtl, setTripDtl] = useState();
    const [pageAble, setPageAble] = useState();

    const pageNum = props.pageNum;
    const perPage = props.perPage;


    useEffect(() => {
        //getTripsPage(1,10,[])
        //getTrips(1,10)
        setMode("list")
        }, []);
          
    const getTrips = async () => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    const params = { pageNum:pageNum, perPage:perPage}
    let response = await axios.get(`${BASE_URL}/trips/searching/multi`,{params});
    console.log(response);
    setTripList(response.data.data);
    setMode("list");
    }          
    
    const getTripsPage = async (_pageNum, _perPage, mbtiArr) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        console.log(mbtiArr);
        const srchMbtia = mbtiArr.A.join(",");
        const srchMbtib = mbtiArr.B.join(",");
        const srchMbtic = mbtiArr.C.join(",");
        const srchMbtid = mbtiArr.D.join(",");
        const params = { pageNum:_pageNum, perPage:_perPage, srchMbtia:srchMbtia, srchMbtib:srchMbtib, srchMbtic:srchMbtic, srchMbtid:srchMbtid}
        let response = await axios.get(`${BASE_URL}/trips/searching/multi`,{params});
        console.log(response);
        setTripList(response.data.data);
        setMode("list");
        } 

          
    const goDtl = async (tripId) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
       // axios.get('http://localhost:5555/trips/'+tripId,{}, {responseType:'json', headers:{"Content-Type": "application/json"}})
        let response = await axios.get(`${BASE_URL}/trips/`+tripId);
        console.log(response);
        setTripDtl(response.data.data);
        console.log(response.data.data);
        setTripId(tripId);
        setPageAble({pageNum:pageNum, perPage:perPage});
        setMode("dtl");
    }   

    function paging(totalPageNum){
        let pageArr = [];
        for (let index = 1; index <= totalPageNum; index++) {
            pageArr.push(<span kye={index} onClick={() => {getTripsPage(index)}}>{index}</span>);
        }
        return pageArr;
    }

    function srching(){
        const arrA = [];
        const arrB = [];
        const arrC = [];
        const arrD = [];
        const mbtiA = document.getElementsByName("mbtiA");
        const mbtiB = document.getElementsByName("mbtiB");
        const mbtiC = document.getElementsByName("mbtiC");
        const mbtiD = document.getElementsByName("mbtiD");

        for (let index = 0; index < mbtiA.length; index++) {
            arrA.push(mbtiA[index].value);
        }
        for (let index = 0; index < mbtiB.length; index++) {
            arrB.push(mbtiB[index].value);
        }
        for (let index = 0; index < mbtiC.length; index++) {
            arrC.push(mbtiC[index].value);
        }
        for (let index = 0; index < mbtiD.length; index++) {
            arrD.push(mbtiD[index].value);
        }
        const arr = {A:arrA, B:arrB, C:arrC, D:arrD};
        getTripsPage(1,10,arr);
    }

    if(mode === "dtl"){
        return (<TripDtl tripDtl={tripDtl} pageAble={pageAble}></TripDtl>);
    }
    if(mode === "list"){
    
        return(
            <div className="tripSrchMulti">
                trip Srch Multi list
                <div>
                    <input type="text" name="mbtiA"></input>
                    <input type="text" name="mbtiB"></input>
                    <input type="text" name="mbtiC"></input>
                    <input type="text" name="mbtiD"></input>
                </div>
                <div>
                    <input type="text" name="mbtiA"></input>
                    <input type="text" name="mbtiB"></input>
                    <input type="text" name="mbtiC"></input>
                    <input type="text" name="mbtiD"></input>
                </div>
                <div><button onClick={srching}>검색</button></div>
                <ul>
                    {tripList && tripList.content.map(trip => (
                        <li key={trip._Id}>
                            <a href="#" onClick={() => {goDtl(trip._Id); return false;} }>{trip.tripNm} ({trip.mbtia + trip.mbtib + trip.mbtic + trip.mbtid})</a>
                        </li>
                    ))}
                </ul>
                <div>
                        {/* {paging(tripList.totalPages)} */}
                        {tripList && paging(tripList.totalPages)}
                </div>
            </div>

        );

    }
}

export default TripSrchMulti;
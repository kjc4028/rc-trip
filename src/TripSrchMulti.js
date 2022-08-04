import axios from "axios";
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
        //getTrips()
        }, []);
          
    const getTrips = async () => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    const params = { pageNum:pageNum, perPage:perPage}
    let response = await axios.get('http://localhost:5555/trips/searching/multi',{params});
    console.log(response);
    setTripList(response.data.data);
    setMode("list");
    }          
    
    const getTripsPage = async (_pageNum, _perPage) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
        const srchMbtia = ["I","E"];
        const srchMbtib = ["S"];
        const srchMbtic = ["F"];
        const srchMbtid = ["J"];
        const params = { pageNum:_pageNum, perPage:_perPage}
        let response = await axios.get('http://localhost:5555/trips/searching/multi',{params});
        console.log(response);
        setTripList(response.data.data);
        setMode("list");
        } 

    //상세화면으로 이동
    // function goDtl(tripId){
    //     axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    //     axios.get('http://localhost:5555/trips/'+tripId,{}, {responseType:'json', headers:{"Content-Type": "application/json"}})
    // .then((res) => {
    //     console.log(res.data.message);
    //     //setData(res.data.message);
    // });
    // }

          
    const goDtl = async (tripId) => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
       // axios.get('http://localhost:5555/trips/'+tripId,{}, {responseType:'json', headers:{"Content-Type": "application/json"}})
        let response = await axios.get('http://localhost:5555/trips/'+tripId);
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

    if(mode === "dtl"){
        return (<TripDtl tripDtl={tripDtl} pageAble={pageAble}></TripDtl>);
    }
    if(mode === "list"){
    
        return(
            <div className="tripList">
                trip list
                <ul>
                    {tripList.content && tripList.content.map(trip => (
                        <li key={trip._Id}>
                            <a href="#" onClick={() => {goDtl(trip._Id); return false;} }>{trip.tripNm}</a>
                        </li>
                    ))}
                </ul>
                <div>
                        {paging(tripList.totalPages)}
                </div>
            </div>

        );

    }
}

export default TripSrchMulti;
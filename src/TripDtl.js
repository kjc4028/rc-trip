import axios from "axios";
import React, { useState, useEffect } from "react";

function TripDtl(props) {
    
    // const [tripDtl, setTripDtl] = useState();
    // const [mode, setMode] = useState(null);
    
    // useEffect(() => {
    //     getTripDtl(props_trip_id)
    //     }, []);
          
    // const getTripDtl = async (tripId) => {
    //     axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    //    // axios.get('http://localhost:5555/trips/'+tripId,{}, {responseType:'json', headers:{"Content-Type": "application/json"}})
    //     let response = await axios.get('http://localhost:5555/trips'+tripId);
    //     console.log(response);
    //     setTripDtl(response.data.data.content);
    // }          
    

    return(
        <div className="tripDtl">
            trip Dtl<br/>
            서비스아이디: <span>{props.tripDtl._Id}</span><br/>
            여행명: <span>{props.tripDtl.tripNm}</span><br/>
            여행내용: <span>{props.tripDtl.tripCts}</span><br/>

        목록
        </div>
    );
}

export default TripDtl;
import axios from "axios";
import React, { useState, useEffect } from "react";
import TripDtl from "./TripDtl";
function TripList() {
    
    const [tripList, setTripList] = useState();
    const [mode, setMode] = useState(null);
    const [tripId, setTripId] = useState(null);
    const [tripDtl, setTripDtl] = useState();


    useEffect(() => {
        getTrips()
        }, []);
          
    const getTrips = async () => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    
    let response = await axios.get('http://localhost:5555/trips');
    console.log(response);
    setTripList(response.data.data.content);
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
        setMode("dtl");
    }   


    if(mode === "dtl"){
        return (<TripDtl tripDtl={tripDtl}></TripDtl>);
    }
    if(mode === "list"){
    
        return(
            <div className="tripList">
                trip list
                <ul>
                    {tripList && tripList.map(trip => (
                        <li key={trip._Id}>
                        <span><a href="#" onClick={() => {goDtl(trip._Id); return false;} }>{trip.tripNm}</a></span>
                        </li>
                    ))}
            </ul>
            </div>
        );
    }
}

export default TripList;
import axios from "axios";
import React, { useState, useEffect } from "react";

function TripList() {
    
    const [tripList, setTripList] = useState();

    useEffect(() => {
        getTrips()
        }, []);
          
    const getTrips = async () => {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    
    let response = await axios.get('http://localhost:5555/trips');
    console.log(response);
    setTripList(response.data.data.content);
    }          
          
    return(
        <div className="tripList">
            trip list
            <ul>
                {tripList && tripList.map(trip => (
                    <li key={trip._Id}>
                    <span>{trip._Id} - {trip.tripNm}</span>
                    </li>
                ))}
        </ul>
        </div>
    );
}

export default TripList;
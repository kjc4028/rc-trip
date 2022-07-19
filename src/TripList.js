import axios from "axios";
import { useState } from "react";

function TripList() {
     axios.defaults.headers.common['Authorization'] = `Bearer `+localStorage.getItem("Authorization");
    axios.get('http://localhost:8080/rest/trips')
    .then((res) => {
console.log(res);
    });
    
    return(
<div className="tripList">tripList</div>

    );
}

export default TripList;
import axios from "axios";
import React, { useState, useEffect } from "react";

function TripList() {
    
    const [tripList, setTripList] = useState(null);
    console.log(localStorage.getItem("Authorization"));
    let listAll = [];    
    // axios.defaults.headers.common['Authorization'] = `Bearer `+localStorage.getItem("Authorization");
    // axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
    // axios.get('http://localhost:8080/rest/trips')
    // .then((res) => {
    //     console.log("res ---");
    //     console.log(res);
    //     //setTripList(res.data.data.content);
    //     console.log(res.data.data.content);
    //     listAll = res.data.data.content;
    //     //setTripList(list);
    // });
    // console.log("listAll ---");
    // console.log(listAll);
    
    
    useEffect(() => {
        const fetchData = async () => {
        axios.defaults.headers.common['Authorization'] = localStorage.getItem("Authorization");
          const response = await axios.get(
            "http://localhost:8080/rest/trips"
          );
          console.log(response.data.data.content);
          listAll = response.data.data.content;
          //let rsList = "<ul>"+"<li>"+response.data.data.content._Id+"</li>"+"</ul>"
          //setTripList(rsList);
        };
        console.log("kjc02---");
        console.log(listAll);
        fetchData();
      }, []);

    return(<>
    <div className="tripList">
        trip list
    </div>
    </>
    );
}

export default TripList;
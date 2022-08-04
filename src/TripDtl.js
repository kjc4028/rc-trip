import axios from "axios";
import React, { useState, useEffect } from "react";
import TripList from "./TripList";

function TripDtl(props) {
    const [mode, setMode] = useState(null); 
    const [pageAble, setPageAble] = useState();   

    function goList(perPage, pageNum){
        setMode("list");
        setPageAble({pageNum:pageNum, perPage:perPage});
    }       
    
if(mode === "list"){
    return <TripList pageAble={pageAble}></TripList>
} else {
    return(
        <div className="tripDtl">
            trip Dtl<br/>
            서비스아이디: <span>{props.tripDtl._Id}</span><br/>
            여행명: <span>{props.tripDtl.tripNm}</span><br/>
            여행내용: <span>{props.tripDtl.tripCts}</span><br/>
            MBTI: {props.tripDtl.mbtia}{props.tripDtl.mbtib}{props.tripDtl.mbtic}{props.tripDtl.mbtid}<br/>
            <button onClick={ () => {goList(props.pageAble.perPage, props.pageAble.pageNum);}}>목록으로</button>
        </div>
    );

}
}

export default TripDtl;
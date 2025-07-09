import axios from "axios";
import React, { useState, useEffect } from "react";
import TripList from "./TripList";

//images
import defaultDtlImg from './images/trip_default_img.jpg';

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
            <section class="py-5">
            <div class="container px-4 px-lg-5 my-5">
                <div class="row gx-4 gx-lg-5 align-items-center">
                    <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src={defaultDtlImg} alt="여행상세 기본 이미지"/></div>
                    <div class="col-md-6">
                        <div class="small mb-1">TRIP-ID : {props.tripDtl._Id}</div>
                        <h1 class="display-5 fw-bolder">{props.tripDtl.tripNm}</h1>
                        <div class="fs-5 mb-5">
                            {/* <span class="text-decoration-line-through">$45.00</span> */}
                            <span>MBTI : {props.tripDtl.mbtia}{props.tripDtl.mbtib}{props.tripDtl.mbtic}{props.tripDtl.mbtid}</span>
                        </div>
                        <p class="lead">{props.tripDtl.tripCts}</p>
                        <div class="d-flex">
                            {/* <input class="form-control text-center me-3" id="inputQuantity" type="num" value="1"   /> */}
                            <button class="btn btn-outline-dark flex-shrink-0" type="button" onClick={ () => {
                                if(props.onBack){ props.onBack('top3'); } else { goList(props.pageAble?.perPage, props.pageAble?.pageNum); }
                            }}>
                                <i class="bi-cart-fill me-1"></i>
                                목록으로
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        </div>
    );

}
}

export default TripDtl;
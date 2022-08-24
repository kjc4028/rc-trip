import axios from "axios";
import { useState } from "react";
import { Link } from "react-router-dom";
import Login from "./Login";

function Header() {

    const [logoutBtn, setLogoutBtn] = useState(null);
    const logoutBtnConst = "";
    // if(localStorage.getItem("Authorization") !== null){
    //     logoutBtnConst = <button>로그아웃</button>;
    // }
    //<button onClick={logout}>로그아웃</button>
    function logout(){
        localStorage.removeItem("Authorization");
        alert("로그아웃 되었습니다.");
        window.location.replace("/");
    }

    function login(){
    }

    return (
        <div className="Header">
          <button onClick={logout}>로그아웃</button><span> | </span>
          <Link to="trip-list">tripList</Link><span> | </span>
          <Link to="trip-srch-multi">tripSrchMult</Link><span> | </span>
        </div>
        )
        
}

export default Header;

import { type } from "@testing-library/user-event/dist/type";
import axios from "axios";
import { useState } from "react";
import Join from "./Join";
import TripList from "./TripList";

//가입결과
function LoginRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

function JoinRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

function Login() {
  
  
  const [data, setData] = useState(null);
  const [mode, setMode] = useState(null);

  function loginBtn(){
    axios.post('http://localhost:8080/user/login',{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      console.log(res);
      console.log("res header " + res.headers.authorization);
      console.log("res data " + res.data);
      setData(res.data);
      let jwtToken = res.headers.authorization;
      //let jwtToken = res.data;
      localStorage.setItem("Authorization", jwtToken);
      setMode('loginSucc');
    });
  }

  function joinBtn(){
    setMode("joinMode");
  }

  
  if(mode === "joinMode"){
    return (
      <Join></Join>
    );
  } else if(mode === "loginSucc"){
    return <TripList></TripList>
  } else {
    return (
      <div className="Loin">
         login Page <br/>
         <input name="userId" id="userId" type="text"></input><br/>
         <input name="userPw" id="userPw" type="password"></input><br/>
         
   
         <button id="loginBtn" onClick={loginBtn}>Login</button>
         <button id="joinBtn" onClick={joinBtn}>Join</button>
         <LoginRs rsdata = {data}></LoginRs>
   
       </div>
     );  
  }

}

export default Login;

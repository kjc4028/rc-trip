
import axios from "axios";
import { useState } from "react";
import Login from "./Login";

//가입결과
function JoinRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

//가입
function Join() {

  const [data, setData] = useState(null);
  const [mode, setMode] = useState(null);

  //가입버튼
  function joinBtn(){
    axios.post('http://localhost:5555/user/signup',{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      console.log(res.data.message);
      setData(res.data.message);
    });
  }

  function loginBtn(){
    setMode("loginMode");
  }

  if(mode === "loginMode"){
    return <Login></Login>
  } else {
  return (
    <div className="Join">
      join Page <br/>
      <input name="userId" id="userId" type="text"></input><br/>
      <input name="userPw" id="userPw" type="text"></input><br/>
      

      <button id="joinBtn" onClick={joinBtn}>Join</button>
      <button id="loginBtn" onClick={loginBtn}>Login</button>
      <JoinRs rsdata={data}></JoinRs>

    </div>
  );
}
}

export default Join;

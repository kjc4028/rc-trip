
import axios from "axios";
import { useState } from "react";

//가입결과
function JoinRs(props){
  if(props.rsdata != null){
    return <p>{props.rsdata}</p>;
  }
}

//가입
function Join() {

  const [data, setData] = useState(null);

  //가입버튼
  function joinBtn(){
    axios.post('http://localhost:8080/user/signup',{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      console.log(res.data.message);
      setData(res.data.message);
    });
  }



  return (
    <div className="Join">
      join Page <br/>
      <input name="userId" id="userId" type="text"></input><br/>
      <input name="userPw" id="userPw" type="text"></input><br/>
      

      <button id="joinBtn" onClick={joinBtn}>join</button>
      <JoinRs rsdata={data}></JoinRs>

    </div>
  );
}

export default Join;

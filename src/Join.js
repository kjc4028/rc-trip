import { type } from "@testing-library/user-event/dist/type";
import axios from "axios";



function Join() {
  function joinBtn(){
    axios.post('http://localhost:8080/user/signup',{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      console.log(res);
    });
  }

  return (
    <div className="Join">
      join Page <br/>
      <input name="userId" id="userId" type="text"></input><br/>
      <input name="userPw" id="userPw" type="text"></input><br/>
      

      <button id="joinBtn" onClick={joinBtn}>join</button>

    </div>
  );
}

export default Join;

import { type } from "@testing-library/user-event/dist/type";
import axios, { BASE_URL } from './axiosConfig';
import { useState } from "react";
import Join from "./Join";
import TripList from "./TripList";
import { Row, Col, Form, Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";
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
    axios.post(`${BASE_URL}/user/login`,{
      userId: document.getElementById("userId").value,
      userPw: document.getElementById("userPw").value
    }, {responseType:'json', headers:{"Content-Type": "application/json"}})
    .then((res) => {
      console.log(res);
      console.log("res header " + res.headers.authorization);
      console.log("res data " + res.data);
      console.log("res header userId" + res.headers);
      setData(res.data);
      let jwtToken = res.headers.authorization;
      let userId = res.data.data.userId;
      //let jwtToken = res.data;
      localStorage.setItem("Authorization", jwtToken);
      localStorage.setItem("userId", userId);
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
    //return <TripList></TripList>
    window.location.replace("/style");
  } else {
    return (
      <div className="Loin">
        <Container className="panel" >            
            <Form>
              <Form.Group className="mb-3" controlId="userId">
                <Form.Label>ID</Form.Label>
                <Form.Control type="text" placeholder="Enter ID" name="userId" xs={4}/>
                <Form.Text className="text-muted">
                  {/* We'll never share your email with anyone else. */}
                </Form.Text>
              </Form.Group>

              <Form.Group className="mb-3" controlId="userPw">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Password" name="userPw"/>
              </Form.Group>

              <Button variant="primary" onClick={loginBtn}>
               Login
              </Button>
              <Button variant="secondary" onClick={joinBtn}>
                Join
              </Button>
            </Form>        
        </Container>
         {/* <input name="userId" id="userId" type="text"></input><br/>
         <input name="userPw" id="userPw" type="password"></input><br/>
         
   
         <button id="loginBtn" onClick={loginBtn}>Login</button>
         <button id="joinBtn" onClick={joinBtn}>Join</button> */}
         <LoginRs rsdata = {data}></LoginRs>
   
       </div>
     );  
  }

}

export default Login;

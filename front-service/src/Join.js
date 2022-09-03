
import axios from "axios";
import { useState } from "react";
import Login from "./Login";
import { Row, Col, Form, Container, Navbar, Nav, NavDropdown, Button } from "react-bootstrap";
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

              <Button variant="secondary" onClick={loginBtn}>
               Login
              </Button>
              <Button variant="primary" onClick={joinBtn}>
                Join
              </Button>
            </Form>        
        </Container>      
      {/* join Page <br/>
      <input name="userId" id="userId" type="text"></input><br/>
      <input name="userPw" id="userPw" type="text"></input><br/>
      

      <button id="joinBtn" onClick={joinBtn}>Join</button>
      <button id="loginBtn" onClick={loginBtn}>Login</button> */}

      <JoinRs rsdata={data}></JoinRs>

    </div>
  );
}
}

export default Join;

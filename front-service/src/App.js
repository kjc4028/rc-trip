import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './Header';
import Login from './Login';
import Join from './Join';
import TripList from './TripList';
import TripDtl from './TripDtl';
import AdminMenu from './AdminMenu';
import TripStyleSelector from "./TripStyleSelector";
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Routes>
          <Route path="/" element={<TripList />} />
          <Route path="/login" element={<Login />} />
          <Route path="/join" element={<Join />} />
          <Route path="/trip/:id" element={<TripDtl />} />
          <Route path="/admin/*" element={<AdminMenu />} />
          <Route path="/style" element={<TripStyleSelector />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

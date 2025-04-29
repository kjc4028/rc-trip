import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import { Link } from "react-router-dom";

import App from './App';

import Join from './Join';
import Login from './Login';
import TripList from './TripList';
import TripSrchMulti from './TripSrchMulti';
import Header from './Header';

import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminMenu from './AdminMenu';
import TripManagement from './TripManagement';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="login" element={<Login />} />
        <Route path="trip-list" element={<TripList />} />
        <Route path="trip-srch-multi" element={<TripSrchMulti />} />
        <Route path="header" element={<Header />} />
        <Route path="admin" element={<AdminMenu />} />
        <Route path="admin/trips" element={<TripManagement />} />
      
      </Routes>
    </BrowserRouter>    
    {/* <App /> */}
    {/* 
    <Login /> */}
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

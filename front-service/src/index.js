import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from './App';
import Login from './Login';
import Join from './Join';
import TripList from './TripList';
import TripSrchMulti from './TripSrchMulti';
import Header from './Header';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminMenu from './AdminMenu';
import TripManagement from './TripManagement';
import TripStyleSelector from "./TripStyleSelector";
import TalkSearch from "./TalkSearch";
import ErrorPage from './ErrorPage';
import AuthRequired from './AuthRequired';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App><Login /></App>} />
        <Route path="/login" element={<App><Login /></App>} />
        <Route path="/join" element={<App><Join /></App>} />
        <Route path="/trip-list" element={<App><TripList /></App>} />
        <Route path="/trip-srch-multi" element={<App><TripSrchMulti /></App>} />
        <Route path="/header" element={<App><Header /></App>} />
        <Route path="/admin" element={<App><AdminMenu /></App>} />
        <Route path="/admin/trips" element={<App><TripManagement /></App>} />
        <Route path="/style" element={<App><TripStyleSelector /></App>} />
        <Route path="/talk" element={<App><TalkSearch /></App>} />
        <Route path="/error" element={<App><ErrorPage /></App>} />
        <Route path="/auth-required" element={<App><AuthRequired /></App>} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();

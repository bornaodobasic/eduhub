import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/homePage/HomePage'; 
import Login from './pages/login/Login'; 
import Registration from './pages/registration/Registration'; 
import Enroll from './pages/enroll/Enroll'; 

import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} /> 
        <Route path="/login" element={<Login />} /> 
        <Route path="/registration" element={<Registration />} /> 
        <Route path="/enroll" element={<Enroll />} /> 
      </Routes>
    </Router>
  );
}

export default App;

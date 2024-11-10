import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/homePage/HomePage'; 
import Login from './pages/login/Login'; 
import Registration from './pages/registration/Registration'; 
import Enroll from './pages/enroll/Enroll'; 
import LogReg from './pages/logReg/LogReg';
import Admin from './pages/admin/Admin';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/homepage" element={<HomePage />} /> 
        <Route path="/login" element={<Login />} /> 
        <Route path="/registration" element={<Registration />} /> 
        <Route path="/enroll" element={<Enroll />} /> 
        <Route path="/login-registration" element={<LogReg />} /> 
        <Route path="/admin" element={<Admin />} />
      </Routes>
    </Router>
  );
}

export default App;

import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Menu from './pages/menu/Menu'; 
import Login from './pages/login/Login'; 
import Registration from './pages/registration/Registration'; 
import Enroll from './pages/enroll/Enroll'; 
import LogReg from './pages/logReg/LogReg';
import Admin from './pages/admin/Admin';
import Homepage from './pages/homepage/Homepage';
import Certificate from './pages/certificate/Certificate';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/menu" element={<Menu />} /> 
        <Route path="/login" element={<Login />} /> 
        <Route path="/registration" element={<Registration />} /> 
        <Route path="/enroll" element={<Enroll />} /> 
        <Route path="/login-registration" element={<LogReg />} /> 
        <Route path="/admin" element={<Admin />} />
        <Route path="/homepage" element={<Homepage/>} />
        <Route path="/certificate" element={<Certificate/>} />
      </Routes>
    </Router>
  );
}

export default App;

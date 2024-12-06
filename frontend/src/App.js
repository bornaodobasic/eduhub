/*
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Menu from './pages/menu/Menu'; 
import Login from './pages/login/Login'; 
import Registration from './pages/registration/Registration'; 
import Enroll from './pages/enroll/Enroll'; 
import LogReg from './pages/logReg/LogReg';
import Admin from './pages/admin/Admin';
import HomePage from './pages/homePage/HomePage';
import Adminhome from './pages/adminHome/AdminHome';
import Certificate from './pages/certificate/Certificate';
import ApproveAdmin from './pages/approveadmin/ApproveAdmin';
import ApproveEmployee from './pages/approveemployee/ApproveEmployee';
import ApproveUcenik from './pages/approveucenik/ApproveUcenik';
import ApproveNastavnik from './pages/approvenastavnik/ApproveNastavnik';
import ApproveRavnatelj from './pages/approveravnatelj/ApproveRavnatelj';
import ApproveSatnicar from './pages/approvesatnicar/ApproveSatnicar';



import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Menu />} /> 
        <Route path="/login" element={<Login />} /> 
        <Route path="/registration" element={<Registration />} /> 
        <Route path="/enroll" element={<Enroll />} /> 
        <Route path="/login-registration" element={<LogReg />} /> 
        <Route path="/admin" element={<Admin />} />
        <Route path="/homepage" element={<HomePage/>} />
        <Route path="/admin-home" element={<Adminhome />} />
        <Route path="/certificate" element={<Certificate/>} />
        <Route path="/approveadmin" element={<ApproveAdmin/>} /> 
        <Route path="/approveemployee" element={<ApproveEmployee/>} />
        <Route path="/approveucenik" element={<ApproveUcenik/>} />
        <Route path="/approvenastavnik" element={<ApproveNastavnik/>} />
        <Route path="/approveravnatelj" element={<ApproveRavnatelj/>} />
        <Route path="/approvesatnicar" element={<ApproveSatnicar/>} />


      </Routes>
    </Router>
  );
}

export default App;
*/

import React from 'react';

function App() {
  const handleLogin = () => {
    // Redirect to the OAuth2 login endpoint in Spring Boot
    window.location.href = "http://localhost:8080/oauth2/authorization/azure-dev";
  };

  return (
    <div style={styles.container}>
      <h1>Welcome to Our Application</h1>
      <p>Click below to log in with Microsoft:</p>
      <button style={styles.button} onClick={handleLogin}>
        Login with Microsoft
      </button>
    </div>
  );
}

const styles = {
  container: {
    textAlign: 'center',
    marginTop: '100px',
    fontFamily: 'Arial, sans-serif',
  },
  button: {
    padding: '10px 20px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#0078D4',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
};

export default App;

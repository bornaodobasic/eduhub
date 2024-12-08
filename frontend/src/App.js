import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Enroll from './pages/enroll/Enroll';
import Admin from './pages/admin/Admin';
import Adminhome from './pages/adminHome/AdminHome';
import Certificate from './pages/certificate/Certificate';
import ApproveAdmin from './pages/approveadmin/ApproveAdmin';
import ApproveEmployee from './pages/approveemployee/ApproveEmployee';
import ApproveUcenik from './pages/approveucenik/ApproveUcenik';
import ApproveNastavnik from './pages/approvenastavnik/ApproveNastavnik';
import ApproveRavnatelj from './pages/approveravnatelj/ApproveRavnatelj';
import ApproveSatnicar from './pages/approvesatnicar/ApproveSatnicar';
import Circles from './components/Circles';
import './App.css';
import Nastavnik from './pages/nastavnik/Nastavnik';
import Ucenik from './pages/ucenik/Ucenik';
import Djelatnik from './pages/djelatnik/Djelatnik';

function App() {
  const handleLogin = () => {
    // Redirect to the OAuth2 login endpoint in Spring Boot
    window.location.href = "http://localhost:8080/oauth2/authorization/azure-dev";
  };

  return (
    <Router>
      <Routes>
        {/* Prijavni ekran */}
        <Route
          path="/"
          element={
            <div className="login-page">
              <div className="heder">
                  <span className="title">eŠkolskaKomunikacija</span>
              </div>

              <Circles />

              <main className="login-form">
                <h1>Dobrodošli!</h1>
                <p className="oneliner">eŠkolskaKomunikacija</p>
                <p>Jedna platforma za sve školske potrebe.</p>
                <button className="continue-button" onClick={handleLogin}>
                  Prijava uz Microsoft
                </button>
              </main>
            </div>
          }
        />

        {/* Ostale rute */}
        <Route path="/enroll" element={<Enroll />} />
        <Route path="/admin-home" element={<Adminhome />} />
        <Route path="/certificate" element={<Certificate />} />
        <Route path="/approveadmin" element={<ApproveAdmin />} />
        <Route path="/approveemployee" element={<ApproveEmployee />} />
        <Route path="/approveucenik" element={<ApproveUcenik />} />
        <Route path="/approvenastavnik" element={<ApproveNastavnik />} />
        <Route path="/approveravnatelj" element={<ApproveRavnatelj />} />
        <Route path="/approvesatnicar" element={<ApproveSatnicar />} />
        <Route path="/nastavnik" element={<Nastavnik />} />
        <Route path="/ucenik" element={<Ucenik />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/djelatnik" element={<Djelatnik />} />
      </Routes>
    </Router>
  );
}

export default App;

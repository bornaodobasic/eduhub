import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Enroll from './pages/enroll/Enroll';
import Admin from './pages/admin/Admin';
import Certificate from './pages/certificate/Certificate';
import Circles from './components/Circles';
import CirclesMobile from './components/CirclesMobile';
import Nastavnik from './pages/nastavnik/Nastavnik';
import Ucenik from './pages/ucenik/Ucenik';
import Djelatnik from './pages/djelatnik/Djelatnik';
import Satnicar from './pages/satnicar/Satnicar';
import Ravnatelj from './pages/ravnatelj/Ravnatelj';

function App() {
  const handleLogin = () => {
    window.location.href = "https://eduhub-rfsg.onrender.com/oauth2/authorization/azure-dev";
  };

  return (
    <Router>
      <Routes>
        
        <Route
          path="/"
          element={
            <div className="login-page">
              <div className="heder">
                  <span className="title">EduHub</span>
              </div>

              <div className='veliki'>
                <Circles />
              </div>
              <div className='maleni'>
                <CirclesMobile />
              </div>

              

              <main className="login-form">
                <h1 className="welcome">Dobrodošli!</h1>
                <p className="oneliner">EduHub</p>
                <p>Jedna platforma za sve školske potrebe.</p>
                <button className="login-button" onClick={handleLogin}>
                  Prijava uz Microsoft
                </button>
              </main>
            </div>
          }
        />

        <Route path="/upis" element={<Enroll />} />
        <Route path="/certificate" element={<Certificate />} />
        <Route path="/nastavnik" element={<Nastavnik />} />
        <Route path="/ucenik" element={<Ucenik />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/djelatnik" element={<Djelatnik />} />
        <Route path="/satnicar" element={<Satnicar />} />
        <Route path="/ravnatelj" element={<Ravnatelj />} />
      </Routes>
    </Router>
  );
}

export default App;

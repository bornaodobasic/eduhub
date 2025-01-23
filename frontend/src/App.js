import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Enroll from './pages/enroll/Enroll';
import Circles from './components/Circles';
import CirclesMobile from './components/CirclesMobile';
import FileUpload from './components/FileUpload';
import Ucenik from './pages/ucenik/Ucenik';
import Nastavnik from './pages/nastavnik/Nastavnik';
import Ravnatelj from './pages/ravnatelj/Ravnatelj';
import Satnicar from './pages/satnicar/Satnicar';
import Djelatnik from './pages/djelatnik/Djelatnik';
import Admin from './pages/admin/Admin';
import Chat from './pages/chat/Chat';
import Chat2 from "./pages/chat2/Chat2";
import 'leaflet/dist/leaflet.css';


function App() {
  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/azure-dev";
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
        <Route path="/upload" element={<FileUpload />} />
        <Route path="/ucenik" element={<Ucenik />} />
        <Route path="/nastavnik" element={<Nastavnik />} />
        <Route path="/ravnatelj" element={<Ravnatelj />} />
        <Route path="/satnicar" element={<Satnicar />} />
        <Route path="/djelatnik" element={<Djelatnik />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/chat2" element={<Chat2 />} />
      </Routes>
    </Router>
  );
}

export default App;
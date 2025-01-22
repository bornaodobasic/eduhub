import React, { useState, useEffect } from "react";
import { FaUserGraduate, FaChalkboardTeacher, FaUserTie, FaClock, FaUsers, FaUserShield, FaSignOutAlt } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUcenik from "../../components/TableUcenik";
import "./Admin.css";
import TableNastavnik from "../../components/TableNastavnik";
import TableRavnatelj from "../../components/TableRavnatelj";
import TableDjelatnik from "../../components/TableDjelatnik";
import TableSatnicar from "../../components/TableSatnicar";
import TableAdmin from "../../components/TableAdmin";
import UcenikForm from "../../components/UcenikForm";
import KorisnikForm from "../../components/KorisnikForm";
import WeatherWidget from "../../components/WeatherWidget";

const Admin = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");
   const [userName, setUserName] = useState(null);
    
    useEffect(() => {
      const fetchUserName = async () => {
          try {
              const response = await fetch('/api/user');
              if (response.ok) {
                  const data = await response.json();
                  setUserName(data.name);
              } else {
                  console.error('Greška pri dohvaćanju emaila:', response.statusText);
              }
          } catch (error) {
              console.error('Došlo je do greške:', error);
          }
      };
    
      fetchUserName();
    }, []);  
  


  const menuItems = [
    { name: "Naslovnica", icon: <FaUserGraduate /> },
    { name: "Učenici", icon: <FaUserGraduate /> },
    { name: "Nastavnici", icon: <FaChalkboardTeacher /> },
    { name: "Ravnatelji", icon: <FaUserTie /> },
    { name: "Satničari", icon: <FaClock /> },
    { name: "Djelatnici", icon: <FaUsers /> },
    { name: "Admini", icon: <FaUserShield /> },
  ];

  const renderContent = () => {
    switch (activeSection) { 
      case "Naslovnica":
        return (
          <div>
              <h1>Pozdrav, {userName}! </h1>
               <WeatherWidget />
          </div>
        );
      case "Učenici":
        return (
            <div className="content-container">
              <div className="top-section">
                 <UcenikForm />
              </div>
                <TableUcenik />
            </div>
          );
      case "Nastavnici":
        return (
          <div className="content-container">
            <div className="top-section">
               <KorisnikForm korisnik="nastavnik" />
            </div>
              <TableNastavnik />
          </div>
        );
      case "Ravnatelji": return (
        <div className="content-container">
          <div className="top-section">
             <KorisnikForm korisnik="ravnatelj" />
          </div>
            <TableRavnatelj />
        </div>
      );
      case "Satničari": return (
        <div className="content-container">
          <div className="top-section">
             <KorisnikForm korisnik="satnicar" />
          </div>
            <TableSatnicar />
        </div>
      );
      case "Djelatnici": return (
        <div className="content-container">
          <div className="top-section">
             <KorisnikForm korisnik="djelatnik" />
          </div>
            <TableDjelatnik />
        </div>
      );
      case "Admini": return (
        <div className="content-container">
          <div className="top-section">
             <KorisnikForm korisnik="admin" />
          </div>
            <TableAdmin />
        </div>
      );
      default:
        return <h1>Odaberite sekciju</h1>;
    }
  };

  return (
    <div className="admin-container">
      <Sidebar activeSection={activeSection} setActiveSection={setActiveSection} menuItems={menuItems}/>
      <div className="admin-content">{renderContent()}</div>
    </div>
  );
};

export default Admin;
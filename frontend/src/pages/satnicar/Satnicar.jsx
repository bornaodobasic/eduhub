import React, { useState, useEffect } from "react";
import { FaBook, FaBell, FaCalendarAlt, FaHome } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import WeatherWidget from "../../components/WeatherWidget";
import Zauzece from "../../components/Zauzece";
import Map from "../../components/Map";
import { IoReturnUpBackOutline } from "react-icons/io5";

const Satnicar = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica"); const [userName, setUserName] = useState(null);
    
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
    { name: "Naslovnica", icon: <FaHome /> },
    { name: "Raspored", icon: <FaCalendarAlt /> },
    { name: "Učionice", icon: <FaBook /> },
  ];


  const handleGenerateSchedule = async () => {
    try {
        const response = await fetch("/api/satnicar/raspored", {
            method: "GET",
        });

        if (response.ok) {
            alert("Raspored je uspješno generiran!");
        } else {
            alert("Dogodila se pogreška pri generiranju rasporeda.");
        }
    } catch (error) {
        console.error("Error generating schedule:", error);
        alert("Dogodila se pogreška pri generiranju rasporeda.");
    }
  };


  const renderContent = () => {
    switch (activeSection) {
      case "Naslovnica":
        return (
          <div>
              <h2>Pozdrav, {userName}! </h2>
               <WeatherWidget />
          </div>
        );
      case "Raspored":
        return  <button className="add-button" onClick={handleGenerateSchedule}>
                 Generiraj Raspored
                </button>
      

      case "Učionice":
        return (
          <div>
              <h2>Tjedno zauzeće učionica </h2>
              <Zauzece />;
          </div>
        );
     
        
      default:
        return <h4>Odaberite sekciju iz izbornika.</h4>;
    }
  };

  return (
    <div className="admin-container">
      <Sidebar activeSection={activeSection} setActiveSection={setActiveSection} menuItems={menuItems} />
      <div className="admin-content">
        {renderContent()}
      </div>
    </div>
  );
};

export default Satnicar;
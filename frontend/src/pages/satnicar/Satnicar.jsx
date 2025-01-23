import React, { useState, useEffect } from "react";
import { FaSchool, FaBell, FaCalendarAlt } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import WeatherWidget from "../../components/WeatherWidget";

import Map from "../../components/Map";

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
    { name: "Naslovnica", icon: <FaCalendarAlt /> },
    { name: "Raspored", icon: <FaCalendarAlt /> },
    { name: "Obavijesti", icon: <FaBell /> },
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
              <h1>Pozdrav, {userName}! </h1>
               <WeatherWidget />
          </div>
        );
      case "Raspored":
        return  <button className="generate-schedule-button" onClick={handleGenerateSchedule}>
                 Generiraj Raspored
                </button>
      
      case "Obavijesti":
        return <Map />;
        
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
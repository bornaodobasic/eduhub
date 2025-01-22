import React, { useState } from "react";
import { FaSchool, FaBell, FaCalendarAlt } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";



const Satnicar = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");

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
        return  <h4>Naslovnica opa opa</h4>
      case "Raspored":
        return  <button className="generate-schedule-button" onClick={handleGenerateSchedule}>
                 Generiraj Raspored
                </button>
      
      case "Obavijesti":
          return <h4>Obavijesti dolaze uskoro!</h4>;
        
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
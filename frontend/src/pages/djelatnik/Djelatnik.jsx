import React, { useState } from "react";
import { FaSchool, FaChartBar, FaChalkboard } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";



const Djelatnik = () => {
  const [activeSection, setActiveSection] = useState("Nekaj");

  const menuItems = [
    { name: "Nekaj", icon: <FaSchool /> },
    { name: "Nesto", icon: <FaChartBar /> },
    { name: "Raspored", icon: <FaChalkboard /> },
  ];

  const renderContent = () => {
    switch (activeSection) {
      case "Nekaj":
        return <h4>Nekaj dolaze uskoro!</h4>;
        
      case "Nesto":
        return <h4>Nesto dolaze uskoro!</h4>;
      
      case "Raspored":
        return <h4>Raspored dolazi uskoro!</h4>;
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

export default Djelatnik;
import React, { useState } from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaComments } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";

const Nastavnik = () => {
  const [activeSection, setActiveSection] = useState("Učenici");

  const menuItems = [
    { name: "Učenici", icon: <FaSchool /> },
    { name: "Predmeti", icon: <FaChartBar /> },
    { name: "Raspored", icon: <FaChalkboard /> },
    { name: "Chat", icon: <FaComments /> },
  ];

  const renderContent = () => {
    switch (activeSection) {
      case "Učenici":
        return <h4>Učeniici dolaze uskoro!</h4>;
      
      case "Predmeti":
        return <h4>Predmeti dolaze uskoro!</h4>;
      
      case "Raspored":
        return <h4>Raspored dolazi uskoro!</h4>;
      
      case "Chat":
        return (
          <div>
            <h4>Dobrodošli u Chat!</h4>
            <button onClick={() => window.location.href = '/chat'}>Idi na Chat</button>
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

export default Nastavnik;
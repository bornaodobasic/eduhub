import React, { useState } from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaCalendarAlt } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUcionice from "../../components/TableUcionice";
import GrafUcionice from "../../components/GrafUcionice";
import UcioniceForm from "../../components/UcioniceForm";
import Izvjestaj from "../../components/Izvjestaj";

import "./Ravnatelj.css";

const Ravnatelj = () => {
  const [activeSection, setActiveSection] = useState("Učionice");

  const menuItems = [
    { name: "Učionice", icon: <FaSchool /> },
    { name: "Učenici", icon: <FaChartBar /> },
    { name: "Izvještaj", icon: <FaChalkboard /> },
    { name: "Raspored", icon: <FaCalendarAlt /> },
  ];

  const handleAddUcionica = async (newUcionica) => {
    try {
      const response = await fetch("/api/ravnatelj/ucionice/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUcionica),
      });

      if (response.ok) {
        alert("Učionica uspješno dodana!");
      } else {
        alert("Došlo je do greške pri dodavanju učionice.");
      }
    } catch (error) {
      console.error("Greška pri dodavanju učionice:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  const renderContent = () => {
    switch (activeSection) {
      case "Učionice":
        return (
          <div className="ucionice-section">
            <div className="graf-i-forma">
              <div className="graf-container">
                <GrafUcionice />
              </div>
              <div className="forma-container">
                <UcioniceForm onAddUcionica={handleAddUcionica} />
              </div>
            </div>
            <TableUcionice />
          </div>
        );
      case "Učenici":
        return <h4>Izvje dolazi uskoro!</h4>;
      case "Izvještaj":
        return (
          <Izvjestaj />
      );
     
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

export default Ravnatelj;
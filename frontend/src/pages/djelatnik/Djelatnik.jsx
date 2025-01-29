import React, { useState , useEffect} from "react";
import { FaSchool, FaChartBar, FaListAlt, FaHome} from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import IzvjestajDjelatnik from "../../components/IzvjestajDjelatnik";
import WeatherWidget from "../../components/WeatherWidget";



const Djelatnik = () => {
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
    { name: "Naslovnica", icon: <FaHome /> },
    { name: "Izvještaj", icon: <FaListAlt /> },
  ];

  const renderContent = () => {
    switch (activeSection) {
      case "Naslovnica":
        return (
          <div>
              <h2>Pozdrav, {userName}! </h2>
               <WeatherWidget />
          </div>
        );    
        case "Izvještaj":
          return <IzvjestajDjelatnik />;
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

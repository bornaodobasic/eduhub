import React, { useState , useEffect} from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaHome} from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
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
    { name: "Nekaj", icon: <FaSchool /> },
    { name: "Nesto", icon: <FaChartBar /> },
    { name: "Raspored", icon: <FaChalkboard /> },
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
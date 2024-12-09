import React from "react";
import { Link } from "react-router-dom";
import Header from "../../components/Header"; 
import "./Nastavnik.css"; 
import "../../components/MainContent.css"; 

const Nastavnik = () => {
    const roles = [
        { name: "Učenici"},
        { name: "Materijali"},
        { name: "Statistika"},
        { name: "Raspored"},
        { name: "Poruke"},
        { name: "Ispiti"},
    ];

    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">
                <aside className="sidebar-left">
                    
                    {roles.map((role) => (
                        <Link key={role.name} to={role.path}>
                            <button className="sidebar-button">{role.name}</button>
                        </Link>
                    ))}
                </aside>

                
                <div className="main-content">
                    {["Obavijest1", "Obavijest2", "Obavijest3"].map((obavijest, index) => (
                        <div key={index} className="notification-box">
                            {obavijest}
                        </div>
                    ))}
                </div>

             
                <aside className="sidebar-right">
                    <div className="empty-container"></div>
                    
                    <div className="weather-widget-container">
                        <div className="weather-widget">
                            <div className="weather-icon">
                                <img 
                                    src={require("../../components/5.png")} 
                                    alt="Weather Icon" 
                                    style={{ width: "50px", height: "50px" }} 
                                />
                            </div> 
                         
                            <p>21°C</p> 
                            
                            <p>Zagreb, Hrvatska</p>
                        </div>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Nastavnik;

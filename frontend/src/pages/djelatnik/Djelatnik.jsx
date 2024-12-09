import React from "react";
import { Link } from "react-router-dom";
import Header from "../../components/Header"; 
import "../../components/MainContent.css"; 

const Djelatnik = () => {
    const roles = [
        { name: "Funkcija1"},
        { name: "Funckija2"},
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

export default Djelatnik;

import React from "react";
import { Link } from "react-router-dom";
import Header from "../../components/Header"; 
import WeatherWidget from "../../components/WeatherWidget";
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
                         <WeatherWidget></WeatherWidget>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Djelatnik;

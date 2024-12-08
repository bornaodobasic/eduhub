import React from "react";
import { Link } from "react-router-dom";
import Header from "../../components/Header2"; // Reusing the provided Header component
import "./Nastavnik.css"; // Styling based on Login
import "../../components/MainContent.css"; // Main content styling

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

                {/* Main Content inline */}
                <div className="main-content">
                    {["Zahtjev1", "Zahtjev2", "Zahtjev3"].map((obavijest, index) => (
                        <div key={index} className="notification-box">
                            {obavijest}
                        </div>
                    ))}
                </div>

                {/* Right Sidebar inline */}
                <aside className="sidebar-right">
                    <div className="empty-container"></div>
                    {/* Weather Widget */}
                    <div className="weather-widget-container">
                        <div className="weather-widget">
                            <div className="weather-icon">
                                <img 
                                    src={require("../../components/5.png")} 
                                    alt="Weather Icon" 
                                    style={{ width: "50px", height: "50px" }} 
                                />
                            </div> 
                         
                            <p>21°C</p> {/* Temperature */}
                            
                            <p>Zagreb, Hrvatska</p> {/* Location */}
                        </div>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Nastavnik;

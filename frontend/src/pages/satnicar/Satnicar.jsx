import React from "react";
import { Link } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "../../components/MainContent.css";

const Satnicar = () => {
    const roles = [
        { name: "Rasporedi" },
        { name: "Učionice" },
        { name: "Oprema" },
    ];

    // Function to handle schedule generation
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
                    <div className="empty-container">
                        <button className="generate-schedule-button" onClick={handleGenerateSchedule}>
                            Generiraj Raspored
                        </button>
                    </div>
                    <div className="weather-widget-container">
                        <WeatherWidget></WeatherWidget>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Satnicar;

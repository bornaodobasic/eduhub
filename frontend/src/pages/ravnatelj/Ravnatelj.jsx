import React, { useState, useEffect } from "react";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "../../components/MainContent.css";

const Ravnatelj = () => {
    const [potvrde, setPotvrde] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [activeSidebarOption, setActiveSidebarOption] = useState("");

    const roles = ["Raspored", "Statistika", "Učionice"];

    useEffect(() => {
        if (activeSidebarOption === "Statistika") {
            fetchPotvrde();
        }
    }, [activeSidebarOption]);

    const fetchPotvrde = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch("/api/ravnatelj/pogledajIzdanePotvrde", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error("Failed to fetch data");
            }

            const data = await response.json();
            setPotvrde(data);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    const renderContent = () => {
        if (activeSidebarOption === "Statistika") {
            if (loading) return <div>Loading...</div>;
            if (error) return <div>Error: {error}</div>;
            return (
                <ul>
                    {potvrde.map((potvrda, index) => (
                        <li key={index}>
                            Ime: {potvrda.imeUcenik}, Prezime: {potvrda.prezimeUcenik}, Datum:{" "}
                            {potvrda.date} {/* Display the datum field as-is */}
                        </li>
                    ))}
                </ul>
            );
        }

        return ["Obavijest1", "Obavijest2", "Obavijest3"].map((obavijest, index) => (
            <div key={index} className="notification-box">
                {obavijest}
            </div>
        ));
    };

    return (
        <div className="homepage">
            <Header/>
            <div className="homepage-container">
            <aside className="sidebar-left">
                    {roles.map((role, index) => (
                        <button
                            key={index}
                            className={`sidebar-button ${activeSidebarOption === role ? "active" : ""}`}
                            onClick={() => setActiveSidebarOption(role)}
                        >
                            {role}
                        </button>
                    ))}
                </aside>

                <div className="main-content">
                    {renderContent()}
                </div>

                <aside className="sidebar-right">
                    <div className="empty-container"></div>
                    <div className="weather-widget-container">
                        <WeatherWidget />
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Ravnatelj;

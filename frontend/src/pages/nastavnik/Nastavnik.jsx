import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import './Nastavnik.css';

const Nastavnik = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [activeRole, setActiveRole] = useState("");
    const [activeSidebarOption, setActiveSidebarOption] = useState("");
    const [leftSidebarOptions, setLeftSidebarOptions] = useState([]);
    const [mainContent, setMainContent] = useState("");



    useEffect(() => {
        switch (activeSidebarOption) {
            case "Učenici":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj učenikaaaa</h4>
                        <form onSubmit={handleUcenikSubmit} >
                            <input type="text" name="imeUcenik" placeholder="Ime učenika" required />
                            <input type="text" name="prezimeUcenik" placeholder="Prezime učenika" required />
                            <input type="text" name="spol" placeholder="Spol (M/Ž)" required  />
                            <input type="text"name="razred" placeholder="Razred" required />
                            <input type="date" name="datumRodenja" required/>
                            <input type="text" name="oib" placeholder="OIB" />
                            <input type="text" name="email" placeholder="Email učenika" required/>

                            <button type="submit">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Materijali":
                break;

            case "Nekaj":
                break;
            
            default:
                setMainContent("Odaberite opciju iz lijevog izbornika.");
        }
    }, [activeSidebarOption]);


    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">

                <aside className="sidebar-left">
                    {leftSidebarOptions.map((option, index) => (
                        <button
                            key={index}
                            className={`sidebar-button ${activeSidebarOption === option ? "active" : ""}`}
                            onClick={() => setActiveSidebarOption(option)}
                        >
                            {option}
                        </button>
                    ))}
                </aside>

                <div className="main-content">
                    <div className="content">{mainContent}</div>
                </div>

                <aside className="sidebar-right">
                    <WeatherWidget></WeatherWidget>
                </aside>
            </div>
        </div>
    );
};

export default Nastavnik;
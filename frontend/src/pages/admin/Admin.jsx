import React, { useState } from "react";
import Header from "../../components/Header"; 
import "../../components/MainContent.css"; 
import './Admin.css';

const Admin = () => {
    const [content, setContent] = useState("Pozdrav, admine!");

    const handleNastavnikSubmit = (e) => {
        e.preventDefault();
        alert("Nastavnik dodan!");
    };

    const handleRavnateljSubmit = (e) => {
        e.preventDefault();
        alert("Ravnatelj dodan!");
    };

    const handleDjelatnikSubmit = (e) => {
        e.preventDefault();
        alert("Djelatnik dodan!");
    };

    const handleSatnicarSubmit = (e) => {
        e.preventDefault();
        alert("Satničar dodan!");
    };

    const handleAdminSubmit = (e) => {
        e.preventDefault();
        alert("Admin dodan!");
    };

    const handleButtonClick = (type) => {
        if (type === "dodajKorisnika") {
            setContent(
                <div className="add-container">
                    {/* Nastavnik */}
                    <div>
                    <h4 className="add-title">Dodaj nastavnika:</h4>
                    <form onSubmit={handleNastavnikSubmit} className="user-form">
                        
                        <input type="text" name="ime" placeholder="Ime nastavnika" className="add-input" />
                        <input type="text" name="prezime" placeholder="Prezime nastavnika" className="add-input" />
                        <input type="email" name="email" placeholder="Email nastavnika" className="add-input" />
                        <button type="submit" className="add-button">DODAJ</button>
                    </form>
                    </div>
                   
                    
                    {/* Ravnatelj */}
                    <div>
                    <h4 className="add-title">Dodaj ravnatelja:</h4>
                    <form onSubmit={handleRavnateljSubmit} className="user-form">
                        
                        <input type="text" name="ime" placeholder="Ime ravnatelja" className="add-input" />
                        <input type="text" name="prezime" placeholder="Prezime ravnatelja" className="add-input" />
                        <input type="email" name="email" placeholder="Email ravnatelja" className="add-input" />
                        <button type="submit" className="add-button">DODAJ</button>
                    </form>
                    </div>
                    
                    {/* Djelatnik */}
                    <div>
                    <h4 className="add-title">Dodaj djelatnika:</h4>
                    <form onSubmit={handleDjelatnikSubmit} className="user-form">
                        
                        <input type="text" name="ime" placeholder="Ime djelatnika" className="add-input" />
                        <input type="text" name="prezime" placeholder="Prezime djelatnika" className="add-input" />
                        <input type="email" name="email" placeholder="Email djelatnika" className="add-input" />
                        <button type="submit" className="add-button">DODAJ</button>
                    </form>
                    </div>

                    {/* Satničar */}
                    <div>
                    <h4 className="add-title">Dodaj satničara:</h4>
                    <form onSubmit={handleSatnicarSubmit} className="user-form">
                       
                        <input type="text" name="ime" placeholder="Ime satničara" className="add-input" />
                        <input type="text" name="prezime" placeholder="Prezime satničara" className="add-input" />
                        <input type="email" name="email" placeholder="Email satničara" className="add-input" />
                        <button type="submit" className="add-button">DODAJ</button>
                    </form>
                    </div>
                    
                    {/* Admin */}
                    <div>
                    <h4 className="add-title">Dodaj admina:</h4>
                    <form onSubmit={handleAdminSubmit} className="user-form">
                       
                        <input type="text" name="ime" placeholder="Ime admina" className="add-input" />
                        <input type="text" name="prezime" placeholder="Prezime admina" className="add-input" />
                        <input type="email" name="email" placeholder="Email admina" className="add-input" />
                        <button type="submit" className="add-button">DODAJ</button>
                    </form>
                    </div>
                </div>
            );
        } else {
            setContent(type);
        }
    };

    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">
                <aside className="sidebar-left">
                    <button 
                        className="sidebar-button" 
                        onClick={() => handleButtonClick("dodajKorisnika")}
                    >
                        Dodaj korisnika
                    </button>
                    <button 
                        className="sidebar-button" 
                        onClick={() => handleButtonClick("Funkcija2")}
                    >
                        Funkcija2
                    </button>
                    <button 
                        className="sidebar-button" 
                        onClick={() => handleButtonClick("Funkcija3")}
                    >
                        Funkcija3
                    </button>
                </aside>

                <div className="main-content">
                    {content}
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

export default Admin;

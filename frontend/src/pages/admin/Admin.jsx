import React, { useState } from "react";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget"
import './Admin.css';

const Admin = () => {
    const [content, setContent] = useState("Dobrodošli na početnu stranicu admina!");

    const handleNastavnikSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/add/nastavnik', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Nastavnik dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati nastavnika'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };


    const handleRavnateljSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/add/ravnatelj', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Ravnatelj dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati ravnatelja'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };


    const handleDjelatnikSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/add/djelatnik', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Djelatnik dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati djelatnika'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };


    const handleSatnicarSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/add/satnicar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Satničar dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati satničara'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };


    const handleAdminSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/add/admin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Admin dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati admina'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
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
            <div className="homepage-container-admin">
                <aside className="sidebar-left-admin">
                    <button 
                        className="sidebar-button-admin" 
                        onClick={() => handleButtonClick("dodajKorisnika")}
                    >
                        Dodaj korisnika
                    </button>
                    <button 
                        className="sidebar-button-admin" 
                        onClick={() => handleButtonClick("Funkcija2")}
                    >
                        Funkcija2
                    </button>
                    <button 
                        className="sidebar-button-admin" 
                        onClick={() => handleButtonClick("Funkcija3")}
                    >
                        Funkcija3
                    </button>
                </aside>

                <div className="main-content-admin">
                    {content}
                </div>

                <aside className="sidebar-right-admin">
                    <div className="empty-container-admin"></div>
                    <div className="weather-widget-container-admin">
                        <WeatherWidget />
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Admin;

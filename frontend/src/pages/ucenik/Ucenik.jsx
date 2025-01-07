import React, { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import Header from "../../components/Header"; 
import WeatherWidget from '../../components/WeatherWidget';
import "../../components/MainContent.css"; 

const Ucenik = () => {
    const roles = [
        { name: "Matematika"},
        { name: "Fizika"},
        { name: "Kemija"},
        { name: "Biologija"},
        { name: "Psihologija"},
        { name: "Filozofija"},
        { name: "Hrvatski jezik"},
        { name: "Engleski jezik"},
        { name: "Latinski jezik"},
        { name: "Geografija"},
        { name: "Povijest"},
        { name: "Tjelesna kultura"},
        { name: "Glazbena kultura"},
    ];

    const userEmail = "uceniknew@eduxhub.onmicrosoft.com"


    const handleDownloadPotvrda = async () => {
        try {
          // Slanje GET zahtjeva na backend za generiranje PDF-a
          const response = await fetch(`/api/ucenik/${userEmail}/generirajPotvrdu`, {
            method: 'GET',
            headers: {
              'Accept': 'application/pdf',  // Očekujemo PDF kao odgovor
            },
          });
    
          // Provjera da li je zahtjev uspio
          if (!response.ok) {
            throw new Error('Došlo je do greške pri preuzimanju PDF-a');
          }
    
          // Preuzimanje PDF podataka
          const blob = await response.blob();
    
          // Kreiraj URL za preuzimanje
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', `potvrda_${userEmail}.pdf`);  // Postavi naziv datoteke
          document.body.appendChild(link);
          link.click();
          
          // Očisti URL objekt nakon što se download završi
          window.URL.revokeObjectURL(url);
        } catch (error) {
          console.error("Greška pri preuzimanju potvrde:", error);
          alert("Došlo je do greške prilikom generiranja potvrde.");
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

                    

                    <button onClick={handleDownloadPotvrda}>
                         POTVRDA
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

export default Ucenik;

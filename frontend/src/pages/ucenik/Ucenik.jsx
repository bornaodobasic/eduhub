import React, { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from '../../components/WeatherWidget';
import "../../components/MainContent.css";

const Ucenik = () => {
    const [predmeti, setPredmeti] = useState([]); // Stanje za predmete
    const [loading, setLoading] = useState(true); // Stanje za praćenje učitavanja
    const [error, setError] = useState(null); // Stanje za praćenje grešaka

    const userEmail = "uceniknew@eduxhub.onmicrosoft.com";

    useEffect(() => {
        // Funkcija za dohvaćanje predmeta
        const fetchPredmeti = async () => {
            try {
                const response = await fetch(`/api/ucenik`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Greška pri dohvaćanju predmeta');
                }

                const data = await response.json(); // Pretvaranje odgovora u JSON
                setPredmeti(data); // Postavljanje predmeta u stanje
            } catch (err) {
                setError(err.message); // Postavljanje greške u stanje
            } finally {
                setLoading(false); // Završetak učitavanja
            }
        };

        fetchPredmeti();
    }, []); // Pražnjenje znači da se useEffect poziva samo jednom

    const handleDownloadPotvrda = async () => {
        try {
            const response = await fetch(`/api/ucenik/${userEmail}/generirajPotvrdu`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/pdf',
                },
            });

            if (!response.ok) {
                throw new Error('Došlo je do greške pri preuzimanju PDF-a');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `potvrda_${userEmail}.pdf`);
            document.body.appendChild(link);
            link.click();
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
                    {loading ? (
                        <p>Učitavanje predmeta...</p>
                    ) : error ? (
                        <p>Greška: {error}</p>
                    ) : predmeti.length > 0 ? (
                        predmeti.map((predmet) => (
                            <Link key={predmet.id} to={`/predmet/${predmet.id}`}>
                                <button className="sidebar-button">{predmet.naziv}</button>
                            </Link>
                        ))
                    ) : (
                        <p>Nema dostupnih predmeta.</p>
                    )}
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


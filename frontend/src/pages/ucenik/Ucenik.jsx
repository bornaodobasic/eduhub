import React, { useState, useEffect } from "react";
import { FaBook, FaTasks, FaCalendarAlt, FaEnvelope, FaCommentDots } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import UcenikPredmeti from "../../components/UcenikPredmeti";
import UcenikAktivnosti from "../../components/UcenikAktivnosti";
import Timetable from "../../components/Timetable";
import UcenikPotvrde from "../../components/UcenikPotvrde";
import "../admin/Admin.css";

const Ucenik = () => {
    const [activeSection, setActiveSection] = useState("Predmeti");
    const [userEmail, setUserEmail] = useState(null);

    const menuItems = [
        { name: "Predmeti", icon: <FaBook /> },
        { name: "Aktivnosti", icon: <FaTasks /> },
        { name: "Raspored", icon: <FaCalendarAlt /> },
        { name: "Potvrde", icon: <FaEnvelope /> },
        { name: "Chat", icon: <FaCommentDots /> },
    ];


    useEffect(() => {
        const fetchUserEmail = async () => {
            try {
                const response = await fetch('/api/user/email');
                if (response.ok) {
                    const data = await response.json();
                    setUserEmail(data.email);
                } else {
                    console.error('Greška pri dohvaćanju emaila:', response.statusText);
                }
            } catch (error) {
                console.error('Došlo je do greške:', error);
            }
        };

        fetchUserEmail();
    }, []);
    const renderContent = () => {
        switch (activeSection) {
            case "Predmeti":
                return (
                    <div className="content-container">
                        <UcenikPredmeti />
                    </div>
                );
            case "Aktivnosti":
                return (
                    <div className="content-container">
                        <UcenikAktivnosti />
                    </div>
                );
            case "Raspored":
                return (
                    <div className="content-container">
                        {userEmail ? <Timetable email={userEmail} /> : <p>Loading timetable...</p>}
                    </div>
                );
            case "Potvrde":
                return (
                    <div className="content-container">
                        {/* Pass the email to UcenikPotvrde as a prop */}
                        {userEmail ? <UcenikPotvrde userEmail={userEmail} /> : <p>Loading...</p>}
                    </div>
                );
            case "Chat":
                return (
                    <div className="content-container">
                        <h1>Ovo je chat učenika</h1>
                        <button onClick={() => (window.location.href = "/chat")}>
            Idi na Chat
          </button>
                    </div>
                );
            default:
                return <h1>Dobrodošli u Učenik panel</h1>;
        }
    };

    return (
        <div className="admin-container">
            <Sidebar activeSection={activeSection} setActiveSection={setActiveSection} menuItems={menuItems} />
            <div className="admin-content">{renderContent()}</div>
        </div>
    );
};

export default Ucenik;

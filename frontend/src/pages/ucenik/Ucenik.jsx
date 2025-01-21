import React, { useState, useEffect } from "react";
import { FaBook, FaTasks, FaCalendarAlt, FaEnvelope, FaCommentDots } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import UcenikPredmeti from "../../components/UcenikPredmeti";
import UcenikAktivnosti from "../../components/UcenikAktivnosti";
import Timetable from "../../components/Timetable";
import UcenikPotvrde from "../../components/UcenikPotvrde";
import "./Ucenik.css";

const Ucenik = () => {
    const [activeSection, setActiveSection] = useState("Predmeti");
    const [userEmail, setUserEmail] = useState(null);
    const [notifications, setNotifications] = useState([]);

    const menuItems = [
        { name: "Naslovnica", icon: <FaBook /> },
        { name: "Predmeti", icon: <FaBook /> },
        { name: "Aktivnosti", icon: <FaTasks /> },
        { name: "Raspored", icon: <FaCalendarAlt /> },
        { name: "Obavijesti", icon: <FaEnvelope /> },
        { name: "Potvrde", icon: <FaEnvelope /> },
        { name: "Chat", icon: <FaCommentDots /> },
    ];

    useEffect(() => {
        const fetchUserEmail = async () => {
            try {
                const response = await fetch("/api/user/email");
                if (response.ok) {
                    const data = await response.json();
                    setUserEmail(data.email);
                } else {
                    console.error("Greška pri dohvaćanju emaila:", response.statusText);
                }
            } catch (error) {
                console.error("Došlo je do greške:", error);
            }
        };

        const fetchNotifications = async () => {
            try {
                const response = await fetch("/api/ucenik/obavijesti");
                if (response.ok) {
                    const data = await response.json();
                    const sortedData = data.sort(
                        (a, b) => new Date(b.datumObavijest) - new Date(a.datumObavijest)
                    );
                    setNotifications(sortedData);
                } else {
                    console.error("Greška pri dohvaćanju obavijesti:", response.statusText);
                }
            } catch (error) {
                console.error("Došlo je do greške pri dohvaćanju obavijesti:", error);
            }
        };

        fetchUserEmail();
        fetchNotifications();
    }, []);

    const renderNotification = (notif) => {
        const isTerenska = notif.adresaLokacija;

        return (
            <div key={notif.sifObavijest} className="notification-item">
                <h3 className="notification-title">{notif.naslovObavijest}</h3>
                <p className="notification-content">{notif.sadrzajObavijest}</p>
                {isTerenska && (
                    <button className="karte-button">Karte</button>
                )}
                <p className="notification-date">
                    {new Date(notif.datumObavijest).toLocaleDateString("hr-HR")}
                </p>
                <hr className="notification-divider" />
            </div>
        );
    };

    const renderContent = () => {
        switch (activeSection) {
            case "Naslovnica":
                return (
                    <div className="content-container">
                        <h4>Najnovije Obavijesti</h4>
                        {notifications.slice(0, 3).map(renderNotification)}
                    </div>
                );
            case "Obavijesti":
                return (
                    <div className="content-container">
                        <h4>Sve Obavijesti</h4>
                        {notifications.map(renderNotification)}
                    </div>
                );
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

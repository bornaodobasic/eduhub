import React, { useState, useEffect } from "react";
import { FaBook, FaTasks, FaCalendarAlt, FaEnvelope, FaCommentDots } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import UcenikPredmeti from "../../components/UcenikPredmeti";
import UcenikAktivnosti from "../../components/UcenikAktivnosti";
import Timetable from "../../components/Timetable";
import UcenikPotvrde from "../../components/UcenikPotvrde";

const Ucenik = () => {
    const [activeSection, setActiveSection] = useState("Predmeti");
    const [userEmail, setUserEmail] = useState(null);
    const [notifications, setNotifications] = useState([]); // State for notifications

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
        // Fetch user email
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

        // Fetch notifications
        const fetchNotifications = async () => {
            try {
                const response = await fetch("/api/ucenik/obavijesti");
                if (response.ok) {
                    const data = await response.json();
                    // Sort notifications by date (newest first)
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

    const renderContent = () => {
        switch (activeSection) {
            case "Naslovnica":
                // Display three newest notifications
                const topThree = notifications.slice(0, 3);
                return (
                    <div className="content-container">
                        <h4>Najnovije Obavijesti</h4>
                        {topThree.map((notif) => (
                            <div key={notif.sifObavijest} className="notification-item">
                                <h5>{notif.naslovObavijest}</h5>
                                <p>{notif.sadrzajObavijest}</p>
                                <p>
                                    Datum: {new Date(notif.datumObavijest).toLocaleDateString("hr-HR")}
                                </p>
                            </div>
                        ))}
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
            case "Obavijesti":
                // Display all notifications
                return (
                    <div className="content-container">
                        <h4>Sve Obavijesti</h4>
                        {notifications.map((notif) => (
                            <div key={notif.sifObavijest} className="notification-item">
                                <h5>{notif.naslovObavijest}</h5>
                                <p>{notif.sadrzajObavijest}</p>
                                <p>
                                    Datum: {new Date(notif.datumObavijest).toLocaleDateString("hr-HR")}
                                </p>
                            </div>
                        ))}
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

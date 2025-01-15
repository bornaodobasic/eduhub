import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "./Ucenik.css";
import Timetable from "../../components/Timetable";

const Ucenik = () => {
    const navigate = useNavigate();

    const [activeSidebarOption, setActiveSidebarOption] = useState("");
    const [leftSidebarOptions, setLeftSidebarOptions] = useState(["Predmeti", "Aktivnosti", "Nekaj"]);
    const [mainContent, setMainContent] = useState("");
    const [subjects, setSubjects] = useState([]);
    const [materials, setMaterials] = useState({});
    const [aktivnosti, setAktivnosti] = useState([]);
    const [expandedSubject, setExpandedSubject] = useState(null);
    const [dostupneAktivnosti, setDostupneAktivnosti] = useState([]);
    const [selectedAktivnosti, setSelectedAktivnosti] = useState([]);

    const [userEmail, setUserEmail] = useState('');

    useEffect(() => {
        if (activeSidebarOption === "Predmeti") {
            fetchSubjectsAndMaterials();
        } else if (activeSidebarOption === "Aktivnosti") {
            fetchAktivnosti();
            fetchDostupneAktivnosti();
        } else {
            setMainContent("Odaberite opciju iz lijevog izbornika.");
        }
    }, [activeSidebarOption]);

    const fetchAktivnosti = async () => {
        try {
            const response = await fetch(`/api/ucenik/aktivnosti/je/${userEmail}`);
            if (!response.ok) throw new Error("Greška prilikom dohvaćanja aktivnosti.");

            const data = await response.json();

            if (data.length === 0) {
                console.log("Nema aktivnosti za ovog učenika.");
            } else {
                setAktivnosti(data);
            }
        } catch (error) {
            console.error(error.message);
        }
    };


    const fetchDostupneAktivnosti = async () => {
        try {
            const response = await fetch(`/api/ucenik/aktivnosti/nije/${userEmail}`);
            if (!response.ok) throw new Error("Greška prilikom dohvaćanja dostupnih aktivnosti.");

            const data = await response.json();

            if (data.length === 0) {
                console.log("Nema dostupnih aktivnosti za ovog učenika.");
            } else {
                setDostupneAktivnosti(data);
            }
        } catch (error) {
            console.error(error.message);
        }
    };


    const handleAddAktivnosti = async () => {
        if (selectedAktivnosti.length === 0) {
            alert("Odaberite barem jednu aktivnost.");
            return;
        }

        try {
            const response = await fetch(`/api/ucenik/dodajAktivnosti`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(selectedAktivnosti),
            });

            if (!response.ok) throw new Error("Greška prilikom dodavanja aktivnosti.");

            alert("Aktivnosti su dodane.");
            fetchAktivnosti();
            setSelectedAktivnosti([]);
        } catch (error) {
            console.error(error.message);
        }
    };

    const handleToggleCheckbox = (aktivnost) => {
        setSelectedAktivnosti((prev) =>
            prev.includes(aktivnost)
                ? prev.filter((a) => a !== aktivnost)
                : [...prev, aktivnost]
        );
    };

    const fetchSubjectsAndMaterials = async () => {
        try {
            const response = await fetch("/api/ucenik/predmeti", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                throw new Error("Failed to fetch subjects");
            }
            const data = await response.json();
            setSubjects(data);
        } catch (error) {
            console.error("Error fetching subjects:", error);
            setMainContent("Greška pri učitavanju predmeta.");
        }
    };

    const fetchMaterials = async (subjectName) => {
        try {
            const response = await fetch(`/api/ucenik/${subjectName}/materijali`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                throw new Error("Failed to fetch materials");
            }
            const data = await response.json();
            setMaterials((prev) => ({ ...prev, [subjectName]: data }));
        } catch (error) {
            console.error("Error fetching materials:", error);
            alert("Greška pri dohvaćanju materijala.");
        }
    };

    const handleDownloadMaterial = async (subjectName, material) => {
        const fileName = material.substring(material.lastIndexOf("/") + 1);
        try {
            const response = await fetch(`/api/ucenik/${subjectName}/materijali/download?suffix=${fileName}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/octet-stream",
                },
            });
            if (!response.ok) {
                throw new Error("Failed to download material");
            }
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error downloading material:", error);
            alert("Greška pri preuzimanju materijala.");
        }
    };

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

    const handleEmailPotvrda = async () => {
        try {
            const response = await fetch(`/api/ucenik/${userEmail}/posaljiMail`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('Mail uspješno poslan!');
            } else {
                const errorMessage = await response.text();
                alert(`Greška pri slanju maila: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Došlo je do greške:', error);
            alert('Došlo je do greške pri slanju maila.');
        }
    };

    const toggleSubject = (subjectName) => {
        if (expandedSubject === subjectName) {
            setExpandedSubject(null);
        } else {
            setExpandedSubject(subjectName);
            if (!materials[subjectName]) {
                fetchMaterials(subjectName);
            }
        }
    };

    const renderAktivnosti = () => (
        <div className="activities-container">
            <h4>Vaše aktivnosti</h4>
            <ul>
                {aktivnosti.map((aktivnost) => (
                    <li key={aktivnost.sifAktivnost}>{aktivnost.oznAktivnost}</li>
                ))}
            </ul>
            <h4>Dostupne aktivnosti</h4>
            <div className="checkbox-form">
                <ul className="checkbox-list">
                    {dostupneAktivnosti.map((aktivnost) => (
                        <li key={aktivnost.sifAktivnost} className="checkbox-item">
                            <label>
                                <input
                                    type="checkbox"
                                    value={aktivnost.oznAktivnost}
                                    checked={selectedAktivnosti.includes(aktivnost.oznAktivnost)}
                                    onChange={() => handleToggleCheckbox(aktivnost.oznAktivnost)}
                                />
                                {aktivnost.oznAktivnost}
                            </label>
                        </li>
                    ))}
                </ul>
                <button onClick={handleAddAktivnosti}>Dodaj</button>
            </div>
        </div>
    );

    const renderSubjects = () => (
        <div className="subjects-container">
            {subjects.map((subject) => (
                <div key={subject.id} className="subject-section">
                    <h3
                        className={`subject-title ${expandedSubject === subject.nazPredmet ? "active" : ""}`}
                        onClick={() => toggleSubject(subject.nazPredmet)}
                    >
                        {subject.nazPredmet}
                    </h3>
                    {expandedSubject === subject.nazPredmet && (
                        <div className="materials-list">
                            <ul>
                                {materials[subject.nazPredmet] && materials[subject.nazPredmet].length > 0 ? (
                                    materials[subject.nazPredmet].map((material) => {
                                        const readableName = material.substring(material.lastIndexOf("/") + 1);
                                        return (
                                            <li key={material} className="material-item">
                                                <button
                                                    onClick={() => handleDownloadMaterial(subject.nazPredmet, material)}
                                                >
                                                    {readableName}
                                                </button>
                                            </li>
                                        );
                                    })
                                ) : (
                                    <p>Nema materijala za ovaj predmet.</p>
                                )}
                            </ul>
                        </div>
                    )}
                </div>
            ))}
        </div>
    );

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
                    {activeSidebarOption === "Predmeti"
                        ? renderSubjects()
                        : activeSidebarOption === "Aktivnosti"
                            ? renderAktivnosti()
                            : <div className="content">{mainContent}</div>}
                    <Timetable email={userEmail} />

                    {activeSidebarOption === "Predmeti" ? renderSubjects() : <div className="content">{mainContent}</div>}
                </div>

                <aside className="sidebar-right">
                    <button onClick={handleDownloadPotvrda}>Preuzmi potvrdu</button>
                    <button onClick={handleEmailPotvrda}>Pošalji potvrdu na mail</button>
                    <button onClick={() => navigate('/chat')} className="chat-button">CHAT</button>
                    <WeatherWidget />
                </aside>

            </div>
        </div>
    );
};

export default Ucenik;

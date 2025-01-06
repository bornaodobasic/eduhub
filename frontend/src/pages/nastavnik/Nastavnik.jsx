import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "./Nastavnik.css";

const Nastavnik = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [activeSidebarOption, setActiveSidebarOption] = useState("");
    const [leftSidebarOptions, setLeftSidebarOptions] = useState(["Učenici", "Materijali", "Nekaj"]);
    const [mainContent, setMainContent] = useState("");
    const [subjects, setSubjects] = useState([]);

    useEffect(() => {
        // Handle content change when sidebar option is selected
        if (activeSidebarOption === "Materijali") {
            fetchSubjectsAndMaterials();
        } else {
            setMainContent("Odaberite opciju iz lijevog izbornika.");
        }
    }, [activeSidebarOption]);

    const fetchSubjectsAndMaterials = async () => {
        try {
            const response = await fetch("/api/nastavnik/predmeti", {
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
            setMainContent(renderMaterials(data));
        } catch (error) {
            console.error("Error fetching subjects:", error);
            setMainContent("Greška pri učitavanju predmeta.");
        }
    };

    const renderMaterials = (subjects) => (
        <div>
            {subjects.map((subject) => (
                <div key={subject.id} className="subject-section">
                    <h3>{subject.nazPredmet}</h3>
                    <button
                        onClick={() => handleAddMaterial(subject.nazPredmet)}
                        className="add-material-btn"
                    >
                        Dodaj materijal
                    </button>
                    <ul>
                        {subject.materijali && subject.materijali.length > 0 ? (
                            subject.materijali.map((material) => (
                                <li key={material}>
                                    <a
                                        href={`https://eduhub-materials.s3.amazonaws.com/${material}`}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                    >
                                        {material.split('/').pop()}
                                    </a>
                                    <button
                                        onClick={() => handleDeleteMaterial(material)}
                                        className="delete-material-btn"
                                    >
                                        Obriši
                                    </button>
                                </li>
                            ))
                        ) : (
                            <p>Nema materijala za ovaj predmet.</p>
                        )}
                    </ul>
                </div>
            ))}
        </div>
    );

    const handleAddMaterial = (subject) => {
        const fileInput = document.createElement("input");
        fileInput.type = "file";
        fileInput.onchange = async (event) => {
            const file = event.target.files[0];
            if (!file) return;

            const formData = new FormData();
            formData.append("file", file);
            formData.append("predmet", subject);

            try {
                const response = await fetch("/api/nastavnik/materijali", {
                    method: "POST",
                    body: formData,
                });
                if (response.ok) {
                    alert("Materijal uspješno dodan!");
                    fetchSubjectsAndMaterials(); // Refresh materials
                } else {
                    alert("Greška pri dodavanju materijala.");
                }
            } catch (error) {
                console.error("Error uploading file:", error);
            }
        };
        fileInput.click();
    };

    const handleDeleteMaterial = async (key) => {
        try {
            const response = await fetch(`/api/nastavnik/materijali?key=${key}`, {
                method: "DELETE",
            });
            if (response.ok) {
                alert("Materijal uspješno obrisan!");
                fetchSubjectsAndMaterials(); // Refresh materials
            } else {
                alert("Greška pri brisanju materijala.");
            }
        } catch (error) {
            console.error("Error deleting file:", error);
        }
    };

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
                    <WeatherWidget />
                </aside>
            </div>
        </div>
    );
};

export default Nastavnik;

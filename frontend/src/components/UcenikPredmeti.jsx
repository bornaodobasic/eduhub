import React, { useState, useEffect } from "react";
import { FaEye, FaArrowLeft, FaDownload } from "react-icons/fa"; // Import the eye icon
import "./TableUcenik.css";
import "./Button.css";

const UcenikPredmeti = () => {
    const [subjects, setSubjects] = useState([]);
    const [materials, setMaterials] = useState({});
    const [expandedSubject, setExpandedSubject] = useState(null);

    useEffect(() => {
        fetchSubjectsAndMaterials();
    }, []);

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

    const formatSubjectName = (name) => {
        let formattedName = name.replace(/_/g, " ");
        formattedName = formattedName.replace(/\s[^ ]*$/, "");
        return formattedName;
    };

    const renderSubjects = () => (
        <div className="table-container">
            <h2 className="table-title">Popis predmeta</h2>
            <table className="table">
                <thead>
                <tr>
                    <th>Predmet</th>
                    <th> </th>
                </tr>
                </thead>
                <tbody>
                {subjects.map((subject) => (
                    <tr key={subject.id}>
                        <td className="subject-name">
                            <h3 className={expandedSubject === subject.nazPredmet ? "active" : ""}>
                                {formatSubjectName(subject.nazPredmet)}
                                <div className="action-icons-right">
                                    <FaEye
                                        className="eye-icon"
                                        onClick={() => toggleSubject(subject.nazPredmet)}
                                        style={{marginLeft: '10px', cursor: 'pointer'}}
                                    />
                                </div>
                            </h3>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

    const renderMaterialsTable = () => {
        return (
            <div className="table-container">
                <h2 className="table-title">{formatSubjectName(expandedSubject)}</h2>
                <table className="table">
                    <thead>
                    <tr>
                        <th>Materijali</th>
                        <th>Akcija</th>
                    </tr>
                    </thead>
                    <tbody>
                    {materials[expandedSubject] && materials[expandedSubject].length > 0 ? (
                        materials[expandedSubject].map((material) => {
                            const readableName = material.substring(material.lastIndexOf("/") + 1);
                            return (
                                <tr key={material}>
                                    <td>{readableName}</td>
                                    <td>
                                        <button
                                            onClick={() => handleDownloadMaterial(expandedSubject, material)}
                                            className="button-small"
                                        >
                                            <span className="button-text">Preuzmi</span>
                                            <FaDownload className="button-icon"/>
                                        </button>
                                    </td>
                                </tr>
                            );
                        })
                    ) : (
                        <tr>
                            <td colSpan="2">Nema materijala za ovaj predmet.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <div className="button-group">
                    <button
                        onClick={() => setExpandedSubject(null)}
                        className="button-small"
                    >
                        <FaArrowLeft className="button-icon-small"/>
                    </button>
                </div>
            </div>
        );
    };

    return (
        <div>
            {expandedSubject ? renderMaterialsTable() : renderSubjects()}
        </div>
    );
};

export default UcenikPredmeti;
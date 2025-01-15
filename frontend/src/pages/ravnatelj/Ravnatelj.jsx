import React, { useState, useEffect } from "react";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "../../components/MainContent.css";

const Ravnatelj = () => {
    const [potvrde, setPotvrde] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [ucionice, setUcionice] = useState([]);
    const [newUcionica, setNewUcionica] = useState({ oznakaUc: "", kapacitet: "" });
    const [activeSidebarOption, setActiveSidebarOption] = useState("");

    const roles = ["Raspored", "Statistika", "Učionice"];

    const fetchUcionice = async () => {
        try {
            const response = await fetch("/api/ravnatelj/ucionice");
            const data = await response.json();
            setUcionice(data);
        } catch (error) {
            console.error("Greška pri dohvaćanju učionica:", error);
        }
    };

    useEffect(() => {
        if (activeSidebarOption === "Statistika") {
            fetchPotvrde();
        }
    }, [activeSidebarOption]);

    const handleAddUcionica = async (event) => {
        event.preventDefault();
        try {
            const response = await fetch("/api/ravnatelj/ucionice/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(newUcionica),
            });

            if (response.ok) {
                const addedUcionica = await response.json();
                setUcionice((prev) => [...prev, addedUcionica]);
                setNewUcionica({ oznakaUc: "", kapacitet: "" }); // Resetiraj formu
                alert("Učionica uspješno dodana!");
            } else {
                const errorMessage = await response.text();
                alert(`Greška: ${errorMessage}`);
            }
        } catch (error) {
            console.error("Greška pri dodavanju učionice:", error);
            alert("Došlo je do pogreške pri komunikaciji s poslužiteljem.");
        }
    };

    const handleDeleteUcionica = async (oznakaUc) => {
        try {
            const response = await fetch(`/api/ravnatelj/ucionice/delete/${oznakaUc}`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify([oznakaUc]),
            });

            if (!response.ok) throw new Error("Greška prilikom brisanja učionice.");

            alert("Učionica obrisana.");
            fetchUcionice();
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

    useEffect(() => {
        if (activeSidebarOption === "Učionice") {
            fetchUcionice();
        }
    }, [activeSidebarOption]);

    const fetchPotvrde = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch("/api/ravnatelj/pogledajIzdanePotvrde", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error("Failed to fetch data");
            }

            const data = await response.json();
            setPotvrde(data);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    const renderContent = () => {
        if (activeSidebarOption === "Statistika") {
            if (loading) return <div>Loading...</div>;
            if (error) return <div>Error: {error}</div>;
            return (
                <ul>
                    {potvrde.map((potvrda, index) => (
                        <li key={index}>
                            Ime: {potvrda.imeUcenik}, Prezime: {potvrda.prezimeUcenik}
                        </li>
                    ))}
                </ul>
            );
        }

        return ["Obavijest1", "Obavijest2", "Obavijest3"].map((obavijest, index) => (
            <div key={index} className="notification-box">
                {obavijest}
            </div>
        ));
    };

    return (
        <div className="homepage">
            <Header/>
            <div className="homepage-container">
                <aside className="sidebar-left">
                    {roles.map((role, index) => (
                        <button
                            key={index}
                            className={`sidebar-button ${activeSidebarOption === role ? "active" : ""}`}
                            onClick={() => setActiveSidebarOption(role)}
                        >
                            {role}
                        </button>
                    ))}
                </aside>

                <div className="main-content">
                    {["Učionice", "Statistika"].includes(activeSidebarOption) ? (
                        activeSidebarOption === "Učionice" ? (
                            <div className="main-content-center">
                                {<div className="main-content-center">
                                    <h4>Popis učionica</h4>
                                    <div className="table-container">
                                        <table className="styled-table">
                                            <thead>
                                            <tr>
                                                <th>Oznaka učionice</th>
                                                <th>Kapacitet</th>
                                                <th>Akcija</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {ucionice.map((ucionica) => (
                                                <tr key={ucionica.oznakaUc}>
                                                    <td>{ucionica.oznakaUc}</td>
                                                    <td>{ucionica.kapacitet}</td>
                                                    <td>
                                                        <button
                                                            onClick={() => handleDeleteUcionica(ucionica.oznakaUc)}
                                                        >
                                                            Obriši
                                                        </button>
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>}
                            </div>
                        ) : (
                            renderContent()
                        )
                    ) : (
                        <p>Odaberite opciju iz lijevog izbornika.</p>
                    )}
                </div>

                <aside className="sidebar-right">
                    {activeSidebarOption === "Učionice" && (
                        <form className="add-ucionica-form" onSubmit={handleAddUcionica}>
                            <h4>Dodaj učionicu</h4>
                            <div>
                                <label>Oznaka učionice:</label>
                                <input
                                    type="text"
                                    value={newUcionica.oznakaUc}
                                    onChange={(e) => setNewUcionica({...newUcionica, oznakaUc: e.target.value})}
                                    required
                                />
                            </div>
                            <div>
                                <label>Kapacitet:</label>
                                <input
                                    type="number"
                                    value={newUcionica.kapacitet}
                                    onChange={(e) => setNewUcionica({
                                        ...newUcionica,
                                        kapacitet: parseInt(e.target.value, 10)
                                    })}
                                    required
                                    min="1"
                                />
                            </div>
                            <button type="submit">Dodaj</button>
                        </form>
                    )}
                    <div className="empty-container"></div>
                    <div className="weather-widget-container">
                        <WeatherWidget/>
                    </div>
                </aside>
            </div>
        </div>
    );
};

export default Ravnatelj;
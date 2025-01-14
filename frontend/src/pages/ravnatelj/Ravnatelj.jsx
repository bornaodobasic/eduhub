import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";

const Ravnatelj = () => {
    const navigate = useNavigate();

    const [activeSidebarOption, setActiveSidebarOption] = useState("");
    const [leftSidebarOptions, setLeftSidebarOptions] = useState(["Učionice", "Statistika", "Nekaj"]);
    const [ucionice, setUcionice] = useState([]);
    const [newUcionica, setNewUcionica] = useState({ oznakaUc: "", kapacitet: "" });

    const fetchUcionice = async () => {
        try {
            const response = await fetch("/api/ravnatelj/ucionice");
            const data = await response.json();
            setUcionice(data);
        } catch (error) {
            console.error("Greška pri dohvaćanju učionica:", error);
        }
    };

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
                    {activeSidebarOption === "Učionice" ? (
                        <div className="main-content-center">
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
                        </div>
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
                                    onChange={(e) => setNewUcionica({ ...newUcionica, oznakaUc: e.target.value })}
                                    required
                                />
                            </div>
                            <div>
                                <label>Kapacitet:</label>
                                <input
                                    type="number"
                                    value={newUcionica.kapacitet}
                                    onChange={(e) => setNewUcionica({ ...newUcionica, kapacitet: parseInt(e.target.value, 10) })}
                                    required
                                    min="1"
                                />
                            </div>
                            <button type="submit">Dodaj</button>
                        </form>
                    )}
                    <WeatherWidget />
                </aside>
            </div>
        </div>
    );
};

export default Ravnatelj;

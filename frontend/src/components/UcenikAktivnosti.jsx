import React, { useState, useEffect } from "react";
import "./TableUcenik.css";

const UcenikAktivnosti = ({ userEmail }) => {
  const [aktivnosti, setAktivnosti] = useState([]);
  const [dostupneAktivnosti, setDostupneAktivnosti] = useState([]);
  const [selectedAktivnosti, setSelectedAktivnosti] = useState([]);

  useEffect(() => {
    if (userEmail) {
      fetchAktivnosti();
      fetchDostupneAktivnosti();
    }
  }, [userEmail]);

  const fetchAktivnosti = async () => {
    try {
      const response = await fetch(`/api/ucenik/aktivnosti/je/${userEmail}`);
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja aktivnosti.");

      const data = await response.json();
      setAktivnosti(data);
    } catch (error) {
      console.error(error.message);
    }
  };

  const fetchDostupneAktivnosti = async () => {
    try {
      const response = await fetch(`/api/ucenik/aktivnosti/nije/${userEmail}`);
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja dostupnih aktivnosti.");

      const data = await response.json();
      setDostupneAktivnosti(data);
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

  const formatActivityName = (name) => name.replace(/_/g, " ");

  return (
      <div className="table-container">
        <h2 className="table-title">Vaše aktivnosti</h2>
        <table className="table">
          <thead>
          <tr>
            <th>Naziv aktivnosti</th>
          </tr>
          </thead>
          <tbody>
          {aktivnosti.map((aktivnost) => (
              <tr key={aktivnost.sifAktivnost}>
                <td>{formatActivityName(aktivnost.oznAktivnost)}</td>
              </tr>
          ))}
          </tbody>
        </table>

        <h2 className="table-title">Dostupne aktivnosti</h2>
        <div className="checkbox-form">
          <table className="table">
            <thead>
            <tr>
              <th>Naziv aktivnosti</th>
            </tr>
            </thead>
            <tbody>
            {dostupneAktivnosti.map((aktivnost) => (
                <tr key={aktivnost.sifAktivnost} className="checkbox-item">
                  <td>
                    <input
                        type="checkbox"
                        value={aktivnost.oznAktivnost}
                        checked={selectedAktivnosti.includes(aktivnost.oznAktivnost)}
                        onChange={() => handleToggleCheckbox(aktivnost.oznAktivnost)}
                    />
                  </td>
                  <td>{formatActivityName(aktivnost.oznAktivnost)}</td>
                </tr>
            ))}
            </tbody>
          </table>
          <div className="button-group">
            <button className="button" onClick={handleAddAktivnosti}>
              Dodaj
            </button>
            <button className="button" onClick={() => setSelectedAktivnosti([])}>
              Poništi odabir
            </button>
          </div>
        </div>
      </div>
  );
};

export default UcenikAktivnosti;

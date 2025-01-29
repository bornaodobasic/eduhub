import React, { useState, useEffect } from "react";
import "./Izvjestaj.css";

const IzvjestajDjelatnik = () => {
  const [potvrde, setPotvrde] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchPotvrde = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch("/api/djelatnik/pogledajIzdanePotvrde", {
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

  useEffect(() => {
    fetchPotvrde();
  }, []);

  const formatDatum = (datum) => {
    const [date, time] = datum.split(" ");
    const [year, month, day] = date.split("-");
    return `${day}.${month}.${year}. u ${time}`;
  };

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">Greška: {error}</p>;

  return (
    <div className="report-container">
      <h2 className="report-title">Izvještaj o preuzetim potvrdama</h2>
      <div className="report-list">
        {potvrde.map((potvrda, index) => (
          <div key={index} className="report-card">
            <p>
              Učenik/ca <strong>{potvrda.imeUcenik} {potvrda.prezimeUcenik} </strong> preuzeo/la je potvrdu o upisu u srednju školu dana <strong>{formatDatum(potvrda.datumGeneriranja)}</strong>.
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default IzvjestajDjelatnik;
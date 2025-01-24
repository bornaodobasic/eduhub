import React, { useState, useEffect } from "react";
import { FaTrashAlt, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";

const TableUcionice = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortConfig, setSortConfig] = useState({ key: "prezimeUcenik", direction: "asc" });

  // Dohvaćanje učionica
  const fetchUcionice = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/ravnatelj/ucionice");
      if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju učionica.");
      const result = await response.json();
      setData(result);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  // Brisanje učionice
  const handleDeleteUcionica = async (oznakaUc) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati učionicu: ${oznakaUc}?`)) return;

    try {
      const response = await fetch(`/api/ravnatelj/ucionice/delete/${oznakaUc}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Greška prilikom brisanja učionice.");

      alert("Učionica obrisana.");
      fetchUcionice(); // Ponovno dohvaćanje podataka nakon brisanja
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  // Sortiranje podataka
  const sortedData = [...data].sort((a, b) => {
    if (a[sortConfig.key] < b[sortConfig.key]) {
      return sortConfig.direction === "asc" ? -1 : 1;
    }
    if (a[sortConfig.key] > b[sortConfig.key]) {
      return sortConfig.direction === "asc" ? 1 : -1;
    }
    return 0;
  });

  const requestSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  const getSortIcon = (key) => {
    if (sortConfig.key === key) {
      return sortConfig.direction === "asc" ? <FaSortUp /> : <FaSortDown />;
    }
    return <FaSort />;
  };

  useEffect(() => {
    fetchUcionice();
  }, []);

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="table-container">
      <h2 className="table-title">Popis svih učionica</h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("oznakaUc")}>
            {getSortIcon("oznakaUc")} Oznaka 
            </th>
            <th onClick={() => requestSort("kapacitet")}>
            {getSortIcon("kapacitet")} Kapacitet 
            </th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((ucionica) => (
            <tr key={ucionica.oznakaUc}>
              <td>{ucionica.oznakaUc}</td>
              <td>{ucionica.kapacitet}</td>
              <td className="action-icons">
                  <FaTrashAlt className="icon delete-icon" title="Obriši" onClick={() => handleDeleteUcionica(ucionica.oznakaUc)}/>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TableUcionice;
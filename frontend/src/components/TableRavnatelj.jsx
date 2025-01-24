import React, { useState, useEffect } from "react";
import { FaTrashAlt, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";
import KorisnikForm from "./KorisnikForm";

const TableRavnatelj = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortConfig, setSortConfig] = useState({ key: "prezimeRavnatelj", direction: "asc" });
    const [prikaziFormu, setPrikaziFormu] = useState(false);
   
     const toggleForma = () => {
       setPrikaziFormu(!prikaziFormu);
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
    const fetchRavnatelji = async () => {
      try {
        const response = await fetch("/api/admin/ravnatelj");
        if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju podataka.");
        const result = await response.json();
        setData(result);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchRavnatelji();
  }, []);

  const handleDelete = async (email) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati ravnatelja s emailom: ${email}?`)) return;

    try {
      const response = await fetch(`/api/admin/ravnatelj/delete/${email}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

      alert("Ravnatelj obrisan.");
      setData(data.filter((ravnatelj) => ravnatelj.email !== email)); // Ažuriraj prikaz
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div>
      <button className="add-button" onClick={toggleForma}>
       
      {prikaziFormu ? 'Gotovo' : 'Dodaj ravnatelja'}
      </button>
      {prikaziFormu &&  <KorisnikForm korisnik="ravnatelj" />}
   
    <div className="table-container">
      <h2 className="table-title">Popis svih ravnatelja</h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("prezimeRavnatelj")}>
            {getSortIcon("prezimeRavnatelj")}Prezime i ime</th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((ravnatelj) => (
            <tr key={ravnatelj.id}>
              <td>
              {ravnatelj.prezimeRavnatelj} {ravnatelj.imeRavnatelj} 
              </td>
              <td>{ravnatelj.email}</td>
              <td className="action-icons">
                <FaTrashAlt
                  className="icon delete-icon"
                  title="Obriši"
                  onClick={() => handleDelete(ravnatelj.email)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
};

export default TableRavnatelj;
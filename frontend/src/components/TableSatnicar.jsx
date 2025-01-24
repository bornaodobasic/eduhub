import React, { useState, useEffect } from "react";
import { FaTrashAlt, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";
import KorisnikForm from "./KorisnikForm";

const TableSatnicar = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);  
  const [sortConfig, setSortConfig] = useState({ key: "prezimeSatnicar", direction: "asc" });
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


    const fetchSatnicari = async () => {
      try {
        const response = await fetch("/api/admin/satnicar");
        if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju podataka.");
        const result = await response.json();
        setData(result);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };


      useEffect(() => {
        fetchSatnicari();
      }, []);

 

  const handleDelete = async (email) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati satničara s emailom: ${email}?`)) return;

    try {
      const response = await fetch(`/api/admin/satnicar/delete/${email}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

      alert("Satničar obrisan.");
      setData(data.filter((satnicar) => satnicar.email !== email)); // Ažuriraj prikaz
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div>
      <button className="add-button" onClick={toggleForma}>
       
      {prikaziFormu ? 'Gotovo' : 'Dodaj satničara'}
      </button>
      {prikaziFormu && <KorisnikForm korisnik="satnicar" onUserAdded={fetchSatnicari} />}

  
    <div className="table-container">
      <h2 className="table-title">Popis svih satničara</h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("prezimeSatnicar")}>
            {getSortIcon("prezimeSatnicar")} Prezime i ime</th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((satnicar) => (
            <tr key={satnicar.id}>
              <td>
               {satnicar.prezimeSatnicar} {satnicar.imeSatnicar}
              </td>
              <td>{satnicar.email}</td>
              <td className="action-icons">
                <FaTrashAlt
                  className="icon delete-icon"
                  title="Obriši"
                  onClick={() => handleDelete(satnicar.email)}
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

export default TableSatnicar;
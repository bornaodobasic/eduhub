import React, { useState, useEffect } from "react";
import { FaEye, FaTrashAlt, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";

const TableAdmin = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
    const [sortConfig, setSortConfig] = useState({ key: "prezimeAdmin", direction: "asc" });
      
      
      
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
    const fetchAdmini = async () => {
      try {
        const response = await fetch("/api/admin/admin");
        if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju podataka.");
        const result = await response.json();
        setData(result);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchAdmini();
  }, []);

  const handleView = (id) => {
    console.log(`Pregled admina sa ID: ${id}`);
  };

  const handleDelete = async (email) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati admina s emailom: ${email}?`)) return;

    try {
      const response = await fetch(`/api/admin/admin/delete/${email}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

      alert("Admin obrisan.");
      setData(data.filter((admin) => admin.email !== email)); // Ažuriraj prikaz
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="table-container">
      <h2 className="table-title">Popis svih admina</h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("prezimeAdmin")}>
            {getSortIcon("prezimeAdmin")} Prezime i ime</th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((admin) => (
            <tr key={admin.id}>
              <td>
              {admin.prezimeAdmin} {admin.imeAdmin} 
              </td>
              <td>{admin.email}</td>
              <td className="action-icons">
                <FaTrashAlt
                  className="icon delete-icon"
                  title="Obriši"
                  onClick={() => handleDelete(admin.email)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TableAdmin;
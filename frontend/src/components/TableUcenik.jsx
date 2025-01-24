import { FaEye, FaTrashAlt, FaArrowLeft, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";
import UcenikForm from "./UcenikForm";
import "./TableUcenik.css";
import { useState, useEffect } from "react";

const TableUcenik = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [viewingActivities, setViewingActivities] = useState(false);
  const [aktivnosti, setAktivnosti] = useState([]);
  const [unusedActivities, setUnusedActivities] = useState([]);
  const [selected, setSelected] = useState([]);
  const [selectedEmail, setSelectedEmail] = useState("");
  const [imePrezime, setImePrezime] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [sortConfig, setSortConfig] = useState({ key: "prezimeUcenik", direction: "asc" });
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

  const fetchUcenici = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/admin/ucenik");
      if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju učenika.");
      const result = await response.json();
      setData(result);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const handleDelete = async (email) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati učenika s emailom: ${email}?`)) return;

    try {
      const response = await fetch(`/api/admin/ucenik/delete/${email}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

      alert("Učenik obrisan.");
      setData(data.filter((ucenik) => ucenik.email !== email)); // Ažuriraj prikaz
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  const fetchAktivnosti = async (email) => {
    try {
      const response = await fetch(`/api/admin/ucenik/aktivnosti/je/${email}`);
      const data = await response.json();
      setAktivnosti(data);

      const unusedResponse = await fetch(`/api/admin/ucenik/aktivnosti/nije/${email}`);
      const unusedData = await unusedResponse.json();
      setUnusedActivities(unusedData);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleView = (email, ime, prezime) => {
    setSelectedEmail(email);
    setImePrezime(`${ime} ${prezime}`);
    fetchAktivnosti(email);
    setViewingActivities(true);
  };

  const handleBack = () => {
    setViewingActivities(false);
    setSelectedEmail("");
    setImePrezime("");
    setUnusedActivities([]);
    setAktivnosti([]);
    setSelected([]);
  };

  const handleAdd = async () => {
    if (selected.length === 0) {
      alert("Odaberite barem jednu aktivnost prije dodavanja.");
      return;
    }

    try {
      const response = await fetch(`/api/admin/ucenik/aktivnosti/add/${selectedEmail}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(selected),
      });

      if (!response.ok) throw new Error("Greška prilikom dodavanja aktivnosti.");

      alert("Aktivnosti dodane.");
      fetchAktivnosti(selectedEmail);
      setSelected([]);
      setShowForm(false);
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  const handleDeleteAktivnost = async (oznAktivnost) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati aktivnost ${oznAktivnost}?`)) return;

    try {
      const response = await fetch(`/api/admin/ucenik/aktivnosti/delete/${selectedEmail}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify([oznAktivnost]),
      });

      if (!response.ok) throw new Error("Greška prilikom brisanja aktivnosti.");

      alert("Aktivnost obrisana.");
      fetchAktivnosti(selectedEmail);
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  const handleCheckboxChange = (item) => {
    setSelected((prev) =>
      prev.includes(item.oznAktivnost)
        ? prev.filter((i) => i !== item.oznAktivnost)
        : [...prev, item.oznAktivnost]
    );
  };

  useEffect(() => {
    fetchUcenici();
  }, []);

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  if (viewingActivities) {
    return (
      <div className="activities-container">
        <h2>
          <FaArrowLeft className="back-icon" onClick={handleBack} /> Aktivnosti učenika: {imePrezime}
        </h2>
        <button className="add-button" onClick={() => setShowForm(!showForm)}>
          {showForm ? "Sakrij aktivnosti" : "Dodaj aktivnosti"}
        </button>
        {showForm && (
          <div className="checkbox-form">
            <div className="checkbox-grid">
              {unusedActivities.map((item) => (
                <label key={item.sifAktivnost} className="checkbox-item">
                  <input
                    type="checkbox"
                    value={item.oznAktivnost}
                    checked={selected.includes(item.oznAktivnost)}
                    onChange={() => handleCheckboxChange(item)}
                  />
                  {item.oznAktivnost.replace(/_/g, ' ')}
                </label>
              ))}
            </div>
            <button className="add-button" onClick={handleAdd}>Dodaj</button>
          </div>
        )}
        <div className="table-container">
        <div className="table-scroll">
          <table className="table">
            <thead>
              <tr>
                <th>Naziv aktivnosti</th>
                <th>Akcija</th>
              </tr>
            </thead>
            <tbody>
              {aktivnosti.map((aktivnost) => (
                <tr key={aktivnost.sifAktivnost}>
                  <td>{aktivnost.oznAktivnost.replace(/_/g, ' ')}</td>
                  <td>
                    <FaTrashAlt
                      className="icon delete-icon"
                      title="Obriši"
                      onClick={() => handleDeleteAktivnost(aktivnost.oznAktivnost)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        </div>
      </div>
    );
  }
  

  return (
    <div>
<button className="add-button" onClick={toggleForma}>
        
{prikaziFormu ? 'Gotovo' : 'Dodaj učenika'}
      </button>
      {prikaziFormu && <UcenikForm onUserAdded={fetchUcenici} />}

    <div className="table-container">
      <h2 className="table-title">Popis svih učenika</h2>
      <table className="table">
        <thead>
          <tr>
          <th onClick={() => requestSort("prezimeUcenik")}>
            {getSortIcon("prezimeUcenik")} Prezime i ime
            </th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((ucenik) => (
            <tr key={ucenik.id}>
              <td>{ucenik.prezimeUcenik} {ucenik.imeUcenik}</td>
              <td>{ucenik.email}</td>
              <td className="action-icons">
                <FaEye
                  className="icon view-icon"
                  title="Pregled"
                  onClick={() => handleView(ucenik.email, ucenik.imeUcenik, ucenik.prezimeUcenik)}
                />
                <FaTrashAlt
                  className="icon delete-icon"
                  title="Obriši"
                  onClick={() => handleDelete(ucenik.email)}
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

export default TableUcenik;
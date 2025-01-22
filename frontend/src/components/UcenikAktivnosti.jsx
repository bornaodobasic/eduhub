import { useState, useEffect } from "react";
import { FaTrashAlt } from "react-icons/fa";

const UcenikAktivnosti = ({ email }) => {
  const [aktivnosti, setAktivnosti] = useState([]);
  const [unusedActivities, setUnusedActivities] = useState([]);
  const [selected, setSelected] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch aktivnosti koje učenik pohađa
  const fetchAktivnosti = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(`/api/ucenik/aktivnosti/je/${email}`);
      if (!response.ok) throw new Error("Neuspješno dohvaćanje aktivnosti.");
      const data = await response.json();
      setAktivnosti(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Fetch aktivnosti koje učenik ne pohađa
  const fetchUnusedActivities = async () => {
    try {
      const response = await fetch(`/api/ucenik/aktivnosti/nije/${email}`);
      if (!response.ok) throw new Error("Neuspješno dohvaćanje neiskorištenih aktivnosti.");
      const data = await response.json();
      setUnusedActivities(data);
    } catch (err) {
      setError(err.message);
    }
  };

  // Dodavanje aktivnosti učeniku
  const handleAdd = async () => {
    if (selected.length === 0) {
      alert("Odaberite barem jednu aktivnost prije dodavanja.");
      return;
    }

    try {
      const response = await fetch(`/api/ucenik/dodajAktivnosti/${email}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(selected),
      });

      if (!response.ok) throw new Error("Greška prilikom dodavanja aktivnosti.");

      alert("Aktivnosti dodane.");
      fetchAktivnosti();
      setSelected([]);
      setShowForm(false);
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };
  

  // Checkbox logika
  const handleCheckboxChange = (oznAktivnost) => {
    setSelected((prev) =>
      prev.includes(oznAktivnost)
        ? prev.filter((item) => item !== oznAktivnost)
        : [...prev, oznAktivnost]
    );
  };

  useEffect(() => {
    fetchAktivnosti();
  }, [email]);

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="activities-container">
      <h2>Aktivnosti</h2>
      <button onClick={() => {
        setShowForm(!showForm);
        if (!showForm) fetchUnusedActivities();
      }}>
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
                  onChange={() => handleCheckboxChange(item.oznAktivnost)}
                />
                {item.oznAktivnost.replace(/_/g, " ")}
              </label>
            ))}
          </div>
          <button onClick={handleAdd}>Dodaj</button>
        </div>
      )}

      <div className="table-container">
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
                <td>{aktivnost.oznAktivnost.replace(/_/g, " ")}</td>
                <td>
                  <FaTrashAlt
                    className="icon delete-icon"
                    title="Obriši"
                    // Ovo je placeholder za buduću funkciju brisanja
                    onClick={() => alert("Funkcija brisanja nije implementirana.")}
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

export default UcenikAktivnosti;

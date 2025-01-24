import React, { useState, useEffect } from "react";
import { FaEye, FaTrashAlt, FaArrowLeft, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";
import KorisnikForm from "./KorisnikForm";

const TableNastavnik = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [viewingSubjects, setViewingSubjects] = useState(false);
  const [predmeti, setPredmeti] = useState([]);
  const [unusedSubjects, setUnusedSubjects] = useState([]);
  const [selected, setSelected] = useState([]);
  const [selectedEmail, setSelectedEmail] = useState("");
  const [imePrezime, setImePrezime] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [sortConfig, setSortConfig] = useState({ key: "prezimeNastavnik", direction: "asc" });
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

  const parsePredmetData = (nazPredmet) => {
    const regex = /^(.+?)_(\d+)_((opca|primat|jezicna))$/i;
    const match = nazPredmet.match(regex);
    if (match) {
      const [_, nazivRaw, razred, smjer] = match;
      const naziv = nazivRaw.replace(/_/g, " ");
      let formattedSmjer = "";
      if (smjer === "opca") formattedSmjer = "Opća";
      else if (smjer === "primat") formattedSmjer = "Prirodoslovno-matematička";
      else if (smjer === "jezicna") formattedSmjer = "Jezična";

      return { naziv, razred, smjer: formattedSmjer };
    }
    return { naziv: nazPredmet.replace(/_/g, " "), razred: "-", smjer: "-" };
  };

  const handleDeletePredmet = async (nazPredmet) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati predmet ${nazPredmet}?`)) return;

    try {
      const response = await fetch(`/api/admin/nastavnik/predmeti/delete/${selectedEmail}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify([nazPredmet]),
      });

      if (!response.ok) throw new Error("Greška prilikom brisanja predmeta.");

      alert("Predmet obrisan.");
      fetchPredmeti(selectedEmail);
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };


  const fetchNastavnici = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/admin/nastavnik");
      if (!response.ok) throw new Error("Došlo je do greške pri dohvaćanju nastavnika.");
      const result = await response.json();
      setData(result);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const handleDelete = async (email) => {
    if (!window.confirm(`Jeste li sigurni da želite obrisati nastavnika s emailom: ${email}?`)) return;

    try {
      const response = await fetch(`/api/admin/nastavnik/delete/${email}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

      alert("Nastavnik obrisan.");
      setData(data.filter((nastavnik) => nastavnik.email !== email));
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  const fetchPredmeti = async (email) => {
    try {
      const response = await fetch(`/api/admin/nastavnik/predmeti/predaje/${email}`);
      const data = await response.json();
      setPredmeti(data);

      const unusedResponse = await fetch(`/api/admin/nastavnik/predmeti/nepredaje/${email}`);
      const unusedData = await unusedResponse.json();
      setUnusedSubjects(unusedData);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleView = (email, ime, prezime) => {
    setSelectedEmail(email);
    setImePrezime(`${ime} ${prezime}`);
    fetchPredmeti(email);
    setViewingSubjects(true);
  };

  const handleBack = () => {
    setViewingSubjects(false);
    setSelectedEmail("");
    setImePrezime("");
    setUnusedSubjects([]);
    setPredmeti([]);
    setSelected([]);
  };

  const handleAdd = async () => {
    if (selected.length === 0) {
      alert("Odaberite barem jedan predmet prije dodavanja.");
      return;
    }

    try {
      const response = await fetch(`/api/admin/nastavnik/predmeti/add/${selectedEmail}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(selected),
      });

      if (!response.ok) throw new Error("Greška prilikom dodavanja predmeta.");

      alert("Predmeti dodani.");
      fetchPredmeti(selectedEmail);
      setSelected([]);
      setShowForm(false);
    } catch (error) {
      alert(`Greška: ${error.message}`);
    }
  };

  const handleCheckboxChange = (item) => {
    setSelected((prev) =>
      prev.includes(item.nazPredmet)
        ? prev.filter((i) => i !== item.nazPredmet)
        : [...prev, item.nazPredmet]
    );
  };

  useEffect(() => {
    fetchNastavnici();
  }, []);

  if (loading) return <p>Učitavanje podataka...</p>;
  if (error) return <p className="error">{error}</p>;

  if (viewingSubjects) {
    const groupedUnusedSubjects = unusedSubjects.reduce((acc, item) => {
      const { naziv, razred, smjer } = parsePredmetData(item.nazPredmet);
      if (!acc[razred]) acc[razred] = {};
      if (!acc[razred][smjer]) acc[razred][smjer] = [];
      acc[razred][smjer].push({ ...item, naziv });
      return acc;
    }, {});

    return (
      <div className="subjects-container">
        <h2>
          <FaArrowLeft className="back-icon" onClick={handleBack} /> Predmeti nastavnika: {imePrezime}
        </h2>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? "Sakrij predmete" : "Dodaj predmete"}
        </button>
        {showForm && (
          <div className="checkbox-form">
            {Object.keys(groupedUnusedSubjects).sort().map((razred) => (
              <div key={razred} className="razred-group">
                <h3>{razred === "-" ? "Ostalo" : `${razred}. razred`}</h3>
                {Object.keys(groupedUnusedSubjects[razred]).sort().map((smjer) => (
                  <div key={smjer} className="smjer-group">
                    <h4>{smjer}</h4>
                    <div className="checkbox-grid">
                      {groupedUnusedSubjects[razred][smjer].map((item) => (
                        <label key={item.sifPredmet} className="checkbox-item">
                          <input
                            type="checkbox"
                            value={item.nazPredmet}
                            checked={selected.includes(item.nazPredmet)}
                            onChange={() => handleCheckboxChange(item)}
                          />
                          {item.naziv}
                        </label>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            ))}
            <button onClick={handleAdd}>Dodaj</button>
          </div>
        )}
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Naziv predmeta</th>
                <th>Razred</th>
                <th>Smjer</th>
                <th>Akcija</th>
              </tr>
            </thead>
            <tbody>
              {predmeti.map((predmet) => {
                const { naziv, razred, smjer } = parsePredmetData(predmet.nazPredmet);

                return (
                  <tr key={predmet.sifPredmet}>
                    <td>{naziv}</td>
                    <td>{razred}</td>
                    <td>{smjer}</td>
                    <td className="action-icons">
                      <FaTrashAlt
                        className="icon delete-icon"
                        title="Obriši"
                        onClick={() => handleDeletePredmet(predmet.nazPredmet)}
                      />
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    );
  }

  return (
    <div>
        <button className="add-button" onClick={toggleForma}>
        {prikaziFormu ? 'Gotovo' : 'Dodaj nastavnika'}
      </button>
      {prikaziFormu &&  <KorisnikForm korisnik="nastavnik" />}
   
    <div className="table-container">
      <h2 className="table-title">Popis svih nastavnika</h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("prezimeNastavnik")}>{getSortIcon("prezimeNastavnik")}Prezime i ime</th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((nastavnik) => (
            <tr key={nastavnik.id}>
              <td>
                {nastavnik.prezimeNastavnik} {nastavnik.imeNastavnik}
              </td>
              <td>{nastavnik.email}</td>
              <td className="action-icons">
                <FaEye
                  className="icon view-icon"
                  title="Pregled"
                  onClick={() =>
                    handleView(nastavnik.email, nastavnik.imeNastavnik, nastavnik.prezimeNastavnik)
                  }
                />
                <FaTrashAlt
                  className="icon delete-icon"
                  title="Obriši"
                  onClick={() => handleDelete(nastavnik.email)}
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

export default TableNastavnik;
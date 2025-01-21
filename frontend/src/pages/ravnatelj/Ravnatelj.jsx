import React, { useState, useEffect } from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaBell, FaTrashAlt } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUcionice from "../../components/TableUcionice";
import GrafUcionice from "../../components/GrafUcionice";
import UcioniceForm from "../../components/UcioniceForm";
import Izvjestaj from "../../components/Izvjestaj";
import "./Ravnatelj.css";

const Ravnatelj = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");
  const [obavijestType, setObavijestType] = useState(null);
  const [obavijesti, setObavijesti] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false);

  const [formData, setFormData] = useState({
    naslovObavijest: "",
    sadrzajObavijest: "",
    adresaLokacija: "",
    gradLokacija: "",
    drzavaLokacija: "",
  });

  const menuItems = [
    { name: "Naslovnica", icon: <FaSchool /> },
    { name: "Učionice", icon: <FaSchool /> },
    { name: "Učenici", icon: <FaChartBar /> },
    { name: "Izvještaj", icon: <FaChalkboard /> },
    { name: "Obavijesti", icon: <FaBell /> },
  ];

  const fetchObavijesti = async () => {
    try {
      const response = await fetch("/api/ravnatelj/obavijesti");
      if (response.ok) {
        const data = await response.json();
        const sortedData = data.sort(
          (a, b) => new Date(b.datumObavijest) - new Date(a.datumObavijest)
        );
        setObavijesti(sortedData);
      } else {
        alert("Greška pri dohvaćanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri dohvaćanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  const deleteObavijest = async (sifObavijest) => {
    try {
      const response = await fetch(`/api/ravnatelj/obavijesti?sifObavijest=${sifObavijest}`, {
        method: "DELETE",
      });

      if (response.ok) {
        alert("Obavijest uspješno izbrisana!");
        setObavijesti((prev) => prev.filter((o) => o.sifObavijest !== sifObavijest));
      } else {
        alert("Došlo je do greške pri brisanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri brisanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  useEffect(() => {
    fetchObavijesti();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddUcionica = async (newUcionica) => {
    try {
      const response = await fetch("/api/ravnatelj/ucionice/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUcionica),
      });

      if (response.ok) {
        alert("Učionica uspješno dodana!");
      } else {
        alert("Došlo je do greške pri dodavanju učionice.");
      }
    } catch (error) {
      console.error("Greška pri dodavanju učionice:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  const handleAddObavijest = async () => {
    const payload =
      obavijestType === "opca"
        ? {
            naslov: formData.naslovObavijest,
            sadrzaj: formData.sadrzajObavijest,
          }
        : {
            naslov: formData.naslovObavijest,
            sadrzaj: formData.sadrzajObavijest,
            odredisteAdresa: formData.adresaLokacija,
            gradOdrediste: formData.gradLokacija,
            drzavaOdrediste: formData.drzavaLokacija,
          };

    try {
      const response = await fetch("/api/ravnatelj/obavijesti", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        alert("Obavijest uspješno dodana!");
        setFormData({
          naslovObavijest: "",
          sadrzajObavijest: "",
          adresaLokacija: "",
          gradLokacija: "",
          drzavaLokacija: "",
        });
        setObavijestType(null);
        fetchObavijesti();
      } else {
        alert("Došlo je do greške pri dodavanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri dodavanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  const renderNotification = (obavijest) => {
    const isTerenska = obavijest.adresaLokacija;

    return (
      <div key={obavijest.sifObavijest} className="notification-item">
        <h3 className="notification-title">{obavijest.naslovObavijest}</h3>
        <p className="notification-content">{obavijest.sadrzajObavijest}</p>
        {isTerenska && <button className="karte-button">Karte</button>}
        <p className="notification-date">
          {new Date(obavijest.datumObavijest).toLocaleDateString("hr-HR")}
        </p>
        {deleteMode && (
          <button
            className="delete-button"
            onClick={() => deleteObavijest(obavijest.sifObavijest)}
          >
            <FaTrashAlt />
          </button>
        )}
        <hr className="notification-divider" />
      </div>
    );
  };

  const renderObavijestiContent = () => {
    if (!obavijestType) {
      return (
        <div className="obavijesti-section">
          <div className="action-buttons">
            <button className="add-button" onClick={() => setObavijestType("opca")}>
              Dodaj Opću Obavijest
            </button>
            <button className="add-button" onClick={() => setObavijestType("terenska")}>
              Dodaj Obavijest o Terenskoj Nastavi
            </button>
          </div>
          <button className="delete-mode-button" onClick={() => setDeleteMode((prev) => !prev)}>
            {deleteMode ? "Gotovo" : "Obriši Obavijesti"}
          </button>
          <div className="obavijesti-list">
            {obavijesti.map(renderNotification)}
          </div>
        </div>
      );
    }

    return (
      <div className="form-container">
        <h4>
          {obavijestType === "opca" ? "Dodaj Opću Obavijest" : "Dodaj Obavijest o Terenskoj Nastavi"}
        </h4>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleAddObavijest();
          }}
        >
          <label>
            Naslov:
            <input
              type="text"
              name="naslovObavijest"
              value={formData.naslovObavijest}
              onChange={handleInputChange}
              required
            />
          </label>
          <label>
            Sadržaj:
            <textarea
              name="sadrzajObavijest"
              value={formData.sadrzajObavijest}
              onChange={handleInputChange}
              required
            />
          </label>
          {obavijestType === "terenska" && (
            <>
              <label>
                Adresa Lokacija:
                <input
                  type="text"
                  name="adresaLokacija"
                  value={formData.adresaLokacija}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label>
                Grad Lokacija:
                <input
                  type="text"
                  name="gradLokacija"
                  value={formData.gradLokacija}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label>
                Država Lokacija:
                <input
                  type="text"
                  name="drzavaLokacija"
                  value={formData.drzavaLokacija}
                  onChange={handleInputChange}
                  required
                />
              </label>
            </>
          )}
          <button type="submit">Dodaj Obavijest</button>
          <button type="button" onClick={() => setObavijestType(null)}>
            Poništi
          </button>
        </form>
      </div>
    );
  };

  const renderContent = () => {
    switch (activeSection) {
      case "Naslovnica":
        return <h4>Naslovnica opa opa</h4>;
      case "Učionice":
        return (
          <div className="ucionice-section">
            <div className="graf-i-forma">
              <div className="graf-container">
                <GrafUcionice />
              </div>
              <div className="forma-container">
                <UcioniceForm onAddUcionica={handleAddUcionica} />
              </div>
            </div>
            <TableUcionice />
          </div>
        );
      case "Učenici":
        return <h4>Učenici dolaze uskoro!</h4>;
      case "Izvještaj":
        return <Izvjestaj />;
      case "Obavijesti":
        return renderObavijestiContent();
      default:
        return <h4>Odaberite sekciju iz izbornika.</h4>;
    }
  };

  return (
    <div className="admin-container">
      <Sidebar
        activeSection={activeSection}
        setActiveSection={setActiveSection}
        menuItems={menuItems}
      />
      <div className="admin-content">{renderContent()}</div>
    </div>
  );
};

export default Ravnatelj;

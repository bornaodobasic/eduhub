import React, { useState } from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaBell } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUcionice from "../../components/TableUcionice";
import GrafUcionice from "../../components/GrafUcionice";
import UcioniceForm from "../../components/UcioniceForm";
import Izvjestaj from "../../components/Izvjestaj";
import "./Ravnatelj.css";

const Ravnatelj = () => {
  const [activeSection, setActiveSection] = useState("Učionice");
  const [obavijestType, setObavijestType] = useState(null); // "opca" ili "terenska"
  const [formData, setFormData] = useState({
    naslovObavijest: "",
    sadrzajObavijest: "",
    adresaLokacija: "",
    gradLokacija: "",
    drzavaLokacija: "",
  });


  const sendFixedObavijest = async () => {
    const payload = {
      naslovObavijest: "Moja Diridika",
      sadrzajObavijest: "Ore na volovolove",
      adresaLokacija: "",
      gradLokacija: "",
      drzavaLokacija: ""
    };
  
    try {
      const response = await fetch("/api/ravnatelj/obavijesti", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
  
      if (response.ok) {
        alert("Obavijest uspješno poslana!");
      } else {
        alert("Došlo je do greške pri slanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri slanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };
  
  const menuItems = [
    { name: "Učionice", icon: <FaSchool /> },
    { name: "Učenici", icon: <FaChartBar /> },
    { name: "Izvještaj", icon: <FaChalkboard /> },
    { name: "Obavijesti", icon: <FaBell /> },
  ];

  // Funkcija za unos u formu
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Dodavanje učionica
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

  // Dodavanje obavijesti
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
      } else {
        alert("Došlo je do greške pri dodavanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri dodavanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  // Brisanje obavijesti
  const handleDeleteObavijest = async (sifObavijest) => {
    try {
      const response = await fetch(`/api/ravnatelj/obavijesti?sifObavijest=${sifObavijest}`, {
        method: "DELETE",
      });

      if (response.ok) {
        alert("Obavijest uspješno izbrisana!");
      } else {
        alert("Došlo je do greške pri brisanju obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri brisanju obavijesti:", error);
      alert("Došlo je do greške pri komunikaciji s poslužiteljem.");
    }
  };

  // Prikaz forme za obavijesti
  const renderObavijestiContent = () => {
    if (!obavijestType) {
      return (
        <div className="obavijesti-section">
          <button onClick={() => setObavijestType("opca")}>Dodaj Opću Obavijest</button>
          <button onClick={() => setObavijestType("terenska")}>Dodaj Obavijest o Terenskoj Nastavi</button>
          <button onClick={sendFixedObavijest}>Pošalji "Moja Diridika"</button>
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

  // Glavni render sekcija
  const renderContent = () => {
    switch (activeSection) {
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
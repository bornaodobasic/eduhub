import React, { useState, useEffect } from "react";
import { FaSchool, FaChartBar, FaChalkboard, FaBell, FaTrashAlt, FaHome } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUcionice from "../../components/TableUcionice";
import GrafUcionice from "../../components/GrafUcionice";
import UcioniceForm from "../../components/UcioniceForm";
import Izvjestaj from "../../components/Izvjestaj";
import WeatherWidget from "../../components/WeatherWidget";
import Map from "../../components/Map";
import "./Ravnatelj.css";

const Ravnatelj = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");
  const [obavijestType, setObavijestType] = useState(null);
  const [obavijesti, setObavijesti] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false); 
  const [userName, setUserName] = useState(null);
  const [showMap, setShowMap] = useState(false); // Stanje koje određuje treba li prikazati mapu
  
  const handleButtonClick = () => {
    setShowMap(!showMap); // Prebacuje stanje između true i false
  };
    
    useEffect(() => {
      const fetchUserName = async () => {
          try {
              const response = await fetch('/api/user');
              if (response.ok) {
                  const data = await response.json();
                  setUserName(data.name);
              } else {
                  console.error('Greška pri dohvaćanju emaila:', response.statusText);
              }
          } catch (error) {
              console.error('Došlo je do greške:', error);
          }
      };
    
      fetchUserName();
    }, []);  
  

  const [formData, setFormData] = useState({
    naslovObavijest: "",
    sadrzajObavijest: "",
    adresaLokacija: "",
    gradLokacija: "",
    drzavaLokacija: "",
  });

  const menuItems = [
    { name: "Naslovnica", icon: <FaHome /> },
    { name: "Učionice", icon: <FaSchool /> },
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
     
  <div className="obavijesti-list">
  {obavijesti.length > 0 ? (
      <div>
        {obavijesti.map((obavijest) => (
            <div key={obavijest.sifObavijest} className="obavijest-item">
              <strong>{obavijest.naslovObavijest}</strong>
              <div>{obavijest.sadrzajObavijest}</div>
             
              {obavijest?.adresaLokacija && obavijest?.gradLokacija && obavijest?.drzavaLoakcija && (
    <div>
      <div>{obavijest.adresaLokacija}, {obavijest.gradLokacija}, {obavijest.drzavaLoakcija}</div>
    <div>
        <button onClick={handleButtonClick}>
          {showMap ? 'Sakrij Karte' : 'Prikaži Karte'}
        </button>

      {showMap && (
        <div>
          <Map street={obavijest.adresaLokacija} city={obavijest.gradLokacija} country={obavijest.drzavaLoakcija}></Map>
      </div>
)}
</div>

</div>
)}

              <div className="obavijest-datum">
                Datum: {new Date(obavijest.datumObavijest).toLocaleString("hr-HR")}
              </div>
              {deleteMode && (
          <button
            className="delete-button"
            onClick={() => deleteObavijest(obavijest.sifObavijest)}
          >
            <FaTrashAlt />
          </button>
        )}
            </div>
        ))}
      </div>
  ) : (
      <p>Nema obavijesti za prikaz.</p>
  )}
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
      <div className="obavijesti-form">
        <h4>
          {obavijestType === "opca" ? "Dodaj Opću Obavijest" : "Dodaj Obavijest o Terenskoj Nastavi"}
        </h4>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleAddObavijest();
          }}
        >

        <div className="form-group">
          <label htmlFor="naslov">
            Naslov obavijesti:
            <input
              type="text"
              name="naslovObavijest"
              value={formData.naslovObavijest}
              placeholder="Unesite naslov obavijesti"
              onChange={handleInputChange}
              required
            />
          </label>
          </div>

          <div className="form-group">
          <label htmlFor="sadrzaj">
            Sadržaj obavijesti:
            <textarea
              name="sadrzajObavijest"
              value={formData.sadrzajObavijest}
              onChange={handleInputChange}
              placeholder="Unesite sadržaj obavijesti"
            rows="5"
              required
            />
          </label>
          </div>


          {obavijestType === "terenska" && (
            <>
              <label>
                <input
                  type="text"
                  name="adresaLokacija"
                  value={formData.adresaLokacija}
                  onChange={handleInputChange}
                  placeholder="Adresa odredišta"
                  required
                />
              </label>
              <label>
                <input
                  type="text"
                  name="gradLokacija"
                  value={formData.gradLokacija}
                  onChange={handleInputChange}
                  placeholder="Grad"
                  required
                />
              </label>
              <label>
                <input
                  type="text"
                  name="drzavaLokacija"
                  value={formData.drzavaLokacija}
                  onChange={handleInputChange}
                  placeholder="Država"
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
        return (
          <div>
              <h2>Pozdrav, {userName}! </h2>
               <WeatherWidget />
          </div>
        );
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
import React, { useState, useEffect } from "react";
import { FaListAlt, FaBook, FaBell, FaTrashAlt, FaHome } from "react-icons/fa";
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
  const [prikaziFormu, setPrikaziFormu] = useState(false);
  const [activeForm, setActiveForm] = useState(null);
  

    const [obavijestNaslov, setObavijestNaslov] = useState("");
    const [obavijestSadrzaj, setObavijestSadrzaj] = useState("");
    const [ulica, setUlica] = useState("");
    const [grad, setGrad] = useState("");
    const [drzava, setDrzava] = useState("");


    const handleSendObavijest = async () => {
      if (!obavijestNaslov || !obavijestSadrzaj) {
        alert("Molimo unesite sve podatke!");
        return;
      }
    
      // Kreiranje podataka u formatu `application/x-www-form-urlencoded`
      const formData = new URLSearchParams();
      formData.append("naslovObavijest", obavijestNaslov);
      formData.append("sadrzajObavijest", obavijestSadrzaj);

    
      try {
        const response = await fetch("/api/ravnatelj/opcaObavijest", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded", // Ključno za @RequestParam
          },
          body: formData.toString(),
        });
    
        if (response.ok) {
          alert("Obavijest uspješno poslana!");
          fetchObavijesti();
          setObavijestNaslov("");
          setObavijestSadrzaj("");
        } else {
          alert("Greška prilikom slanja obavijesti.");
        }
      } catch (error) {
        console.error("Greška pri slanju obavijesti:", error);
        alert("Došlo je do greške!");
      }
    };
  
    const handleSendTerenskaObavijest = async () => {
      if (!obavijestNaslov || !obavijestSadrzaj || !ulica || !grad || !drzava) {
        alert("Molimo unesite sve podatke!");
        return;
      }
    
      // Kreiranje podataka u formatu `application/x-www-form-urlencoded`
      const formData = new URLSearchParams();
      formData.append("naslovObavijest", obavijestNaslov);
      formData.append("sadrzajObavijest", obavijestSadrzaj);
      formData.append("adresaLokacija", ulica);
      formData.append("gradLokacija", grad);
      formData.append("drzavaLokacija", drzava);

    
      try {
        const response = await fetch("/api/ravnatelj/terenskaObavijest", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded", // Ključno za @RequestParam
          },
          body: formData.toString(),
        });
    
        if (response.ok) {
          alert("Obavijest o terenskoj nastavi uspješno poslana!");
          fetchObavijesti();
          setObavijestNaslov("");
          setObavijestSadrzaj("");
        } else {
          alert("Greška prilikom slanja obavijesti.");
        }
      } catch (error) {
        console.error("Greška pri slanju obavijesti:", error);
        alert("Došlo je do greške!");
      }
    };
    
const renderObavijestiForm = () => (
  <div className="obavijesti-form">
    <h2>Dodaj novu obavijest</h2>
    <div className="form-group">
      <label htmlFor="naslov">Naslov obavijesti:</label>
      <input
        type="text"
        id="naslov"
        value={obavijestNaslov}
        onChange={(e) => setObavijestNaslov(e.target.value)}
        placeholder="Unesite naslov obavijesti"
      />
    </div>
    <div className="form-group">
      <label htmlFor="sadrzaj">Sadržaj obavijesti:</label>
      <textarea
        id="sadrzaj"
        value={obavijestSadrzaj}
        onChange={(e) => setObavijestSadrzaj(e.target.value)}
        placeholder="Unesite sadržaj obavijesti"
        rows="5"
      ></textarea>
    </div>
    <button
      onClick={handleSendObavijest}
      disabled={!obavijestNaslov || !obavijestSadrzaj}
    >
      Pošalji obavijest
    </button>
  </div>
);

const renderTerenskaObavijestiForm = () => (
  <div className="obavijesti-form">
    <h2>Dodaj novu obavijest o terenskoj nastavi</h2>
    <div className="form-group">
      <label htmlFor="naslov">Naslov obavijesti:</label>
      <input
        type="text"
        id="naslov"
        value={obavijestNaslov}
        onChange={(e) => setObavijestNaslov(e.target.value)}
        placeholder="Unesite naslov obavijesti"
      />
    </div>
    <div className="form-group">
      <label htmlFor="sadrzaj">Sadržaj obavijesti:</label>
      <textarea
        id="sadrzaj"
        value={obavijestSadrzaj}
        onChange={(e) => setObavijestSadrzaj(e.target.value)}
        placeholder="Unesite sadržaj obavijesti"
        rows="5"
      ></textarea>
    </div>
    <div className="form-group">
      <label htmlFor="ulica">Ulica:</label>
      <input
        type="text"
        id="ulica"
        value={ulica}
        onChange={(e) => setUlica(e.target.value)}
        placeholder="Unesite ulicu odredišta"
      />
    </div>
    <div className="form-group">
      <label htmlFor="grad">Grad:</label>
      <input
        type="text"
        id="grad"
        value={grad}
        onChange={(e) => setGrad(e.target.value)}
        placeholder="Unesite grad odredišta"
      />
    </div>
    <div className="form-group">
      <label htmlFor="drzava">Država:</label>
      <input
        type="text"
        id="drzava"
        value={drzava}
        onChange={(e) => setDrzava(e.target.value)}
        placeholder="Unesite državu odredišta"
      />
    </div>
    <button
      onClick={handleSendTerenskaObavijest}
      disabled={!obavijestNaslov || !obavijestSadrzaj || !ulica || !grad || !drzava}
    >
      Pošalji obavijest
    </button>
  </div>
);



    const toggleForma = () => {
      setPrikaziFormu(!prikaziFormu);
    };
  
    const [prikaziGraf, setPrikaziGraf] = useState(false);
  
    const toggleGraf = () => {
      setPrikaziGraf(!prikaziGraf);
    };
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
    { name: "Učionice", icon: <FaBook /> },
    { name: "Izvještaj", icon: <FaListAlt /> },
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
        <button className="karte-button" onClick={handleButtonClick}>
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

              <div className="forma-container">

                 <button className="add-button" onClick={toggleForma}>
                   {prikaziFormu ? 'Gotovo' : 'Dodaj učionicu'}
                 </button>
                  {prikaziFormu &&  <UcioniceForm onAddUcionica={handleAddUcionica} /> }
               
              </div>
              <div className="graf-container">

                 <button className="add-button" onClick={toggleGraf}>
                   {prikaziGraf ? 'Sakrij graf' : 'Prikaži graf'}
                 </button>
                  {prikaziGraf &&  <GrafUcionice />}
               
              </div>

            </div>
            <TableUcionice />
          </div>
        );
      case "Izvještaj":
        return <Izvjestaj />;
      case "Obavijesti":{
        return (
          <div >
            <div className="button-group">
              <button className="add-button" onClick={() => setActiveForm("obavijest")}>
                Dodaj opću obavijest
              </button>
              <button className="add-button" onClick={() => setActiveForm("terenska")}>
                Dodaj obavijest o terenskoj nastavi
              </button>
            </div>
    
            {activeForm === "obavijest" && renderObavijestiForm()}
            {activeForm === "terenska" && renderTerenskaObavijestiForm()}


            <button className="delete-mode-button" onClick={() => setDeleteMode((prev) => !prev)}>
            {deleteMode ? "Gotovo" : "Obriši Obavijesti"}
          </button>
          <div className="obavijesti-list">
            {renderNotification(obavijesti)}
          </div>
          </div>
        );
        }
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
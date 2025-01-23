import React, { useState, useEffect } from "react";
import { Navigate } from 'react-router-dom';
import { FaBook, FaTasks, FaCalendarAlt, FaEnvelope, FaCommentDots, FaMap} from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import Timetable from "../../components/Timetable";
import UcenikPotvrde from "../../components/UcenikPotvrde";
import MapRoute from "../../components/MapRoute";
import UcenikAktivnosti from "../../components/UcenikAktivnosti";
import WeatherWidger from "../../components/WeatherWidget";
import Map from "../../components/Map";

import "./Ucenik.css";

const Ucenik = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState(null);
  const [activeTab, setActiveTab] = useState("Materijali");
  const [materials, setMaterials] = useState([]);
  const [userEmail, setUserEmail] = useState(null);
  const [obavijesti, setObavijesti] = useState([]); // Lista obavijesti
  const [latestObavijesti, setLatestObavijesti] = useState([]);
  const [userName, setUserName] = useState(null);

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



  const fetchLatestObavijesti = async () => {
    try {
      const response = await fetch("/api/ucenik/obavijesti", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja obavijesti.");
      const data = await response.json();

      // Sortiraj obavijesti od najnovije prema najstarijoj
      const sortedData = data.sort((a, b) => new Date(b.datumObavijest) - new Date(a.datumObavijest));

      // Uzmi samo prve tri obavijesti
      setLatestObavijesti(sortedData.slice(0, 3));
    } catch (error) {
      console.error("Greška pri dohvaćanju obavijesti:", error);
    }
  };


  const fetchGlobalObavijesti = async () => {
    try {
      const response = await fetch("/api/ucenik/obavijesti", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja obavijesti.");
      const data = await response.json();

      // Sortiraj obavijesti od najnovije prema najstarijoj
      const sortedData = data.sort((a, b) => new Date(b.datumObavijest) - new Date(a.datumObavijest));
      setObavijesti(sortedData);
    } catch (error) {
      console.error("Greška pri dohvaćanju obavijesti:", error);
    }
  };


  useEffect(() => {
    if (activeTab === "Obavijesti" && selectedSubject) {
      fetchObavijesti();
    }
  }, [activeTab, selectedSubject]);

  useEffect(() => {
    if (activeSection === "Obavijesti") {
      fetchGlobalObavijesti();
    }
  }, [activeSection]);

  useEffect(() => {
    if (activeSection === "Naslovnica") {
      fetchLatestObavijesti();
    }
  }, [activeSection]);




  const fetchObavijesti = async () => {
    if (!selectedSubject) {
      console.error("Predmet nije odabran.");
      return;
    }

    try {
      const response = await fetch(`/api/ucenik/${selectedSubject}/obavijesti`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja obavijesti.");
      const data = await response.json();

      // Sortiraj obavijesti od najnovije prema najstarijoj
      const sortedData = data.sort((a, b) => new Date(b.datumObavijest) - new Date(a.datumObavijest));
      setObavijesti(sortedData);
    } catch (error) {
      console.error("Greška pri dohvaćanju obavijesti:", error);
    }
  };
  


  const renderObavijestiList = () => (
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
    <Map street={obavijest.adresaLokacija} city={obavijest.gradLokacija} country={obavijest.drzavaLoakcija}></Map>
  </div>
)}
    
                    <div className="obavijest-datum">
                      Datum: {new Date(obavijest.datumObavijest).toLocaleString("hr-HR")}
                    </div>
                  </div>
              ))}
            </div>
        ) : (
            <p>Nema obavijesti za prikaz.</p>
        )}
      </div>
  );



  useEffect(() => {
    const fetchUserEmail = async () => {
      try {
        const response = await fetch('/api/user/email');
        if (response.ok) {
          const data = await response.json();
          setUserEmail(data.email);
        } else {
          console.error('Greška pri dohvaćanju emaila:', response.statusText);
        }
      } catch (error) {
        console.error('Došlo je do greške:', error);
      }
    };

    fetchUserEmail();
  }, []);

  const parseSubject = (nazPredmet) => {
    const match = nazPredmet.match(/(\D+)(\d)(.*)/); // Regularni izraz za razdvajanje
    if (!match) return { naziv: "", razred: "", smjer: "" };

    const naziv = match[1].replace(/_/g, " ").trim(); // Sve lijevo od broja
    const razred = match[2]; // Broj (razred)
    const smjer = match[3].trim(); // Sve desno od broja

    // Formatiranje smjera
    const formattedSmjer =
        smjer === "_opca"
            ? "Opća gimnazija"
            : smjer === "_jezicna"
                ? "Jezična gimnazija"
                : smjer === "_primat"
                    ? "Prirodoslovno-matematička gimnazija"
                    : smjer;

    return {
      naziv: naziv.replace(/\b\w/g, (c) => c.toUpperCase()), // Velika slova za prvu riječ
      razred: `${razred}. razred`,
      smjer: formattedSmjer,
    };
  };



  // Fetch za materijale
  const fetchMaterials = async () => {
    try {
      const response = await fetch(`/api/ucenik/${selectedSubject}/materijali`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja materijala.");
      const data = await response.json();
      setMaterials(data);
    } catch (error) {
      console.error("Error fetching materials:", error);
    }
  };

  // Dohvati predmete
  const fetchSubjects = async () => {
    try {
      const response = await fetch("/api/ucenik/predmeti", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja predmeta.");
      const data = await response.json();
      setSubjects(data);
    } catch (error) {
      console.error(error.message);
    }
  };

  useEffect(() => {
    if (activeSection === "Predmeti") {
      fetchSubjects();
    }
  }, [activeSection]);

  useEffect(() => {
    if (selectedSubject) {
      fetchMaterials();
    }
  }, [selectedSubject]);

  useEffect(() => {
    setSelectedSubject(null);
    setActiveTab("Materijali");
  }, [activeSection]);



  const renderSubjects = () => (
      <div className="subjects-grid">
        {subjects.map((subject) => {
          const { naziv, razred, smjer } = parseSubject(subject.nazPredmet);

          return (
              <div
                  key={subject.id}
                  className="subject-card"
                  onClick={() => setSelectedSubject(subject.nazPredmet)}
              >
                <div className="subject-name">{naziv}</div>
                <div className="subject-razred">{razred}</div>
                <div className="subject-smjer">{smjer}</div>
              </div>
          );
        })}
      </div>
  );



  const renderMaterials = () => (
      <div className="materials-grid">
        {materials.length > 0 ? (
            materials.map((material) => (
                <div key={material} className="material-card">
                  <div className="material-icon">
                    <img src="/path/to/pdf-icon.png" alt="PDF" />
                  </div>
                  <div className="material-details">
                    <a
                        href={`https://eduhub-materials.s3.amazonaws.com/${material}`}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="material-link"
                    >
                      {material.split("_").pop()}
                    </a>
                  </div>
                </div>
            ))
        ) : (
            <p>Nema materijala za ovaj predmet.</p>
        )}
      </div>
  );

  const renderContent = () => {
    if (activeSection === "Naslovnica") {
      return (
          <div>

            <h1>Pozdrav, {userName}! </h1>
            <div className="latest-obavijesti">
              <h3>Najnovije Obavijesti</h3>

              {latestObavijesti.length > 0 ? (
                  latestObavijesti.map((obavijest) => (
                      <div key={obavijest.sifObavijest} className="obavijest-item">
                        <strong>{obavijest.naslovObavijest}</strong>
                        <div>{obavijest.sadrzajObavijest}</div>
                        <div className="obavijest-datum">
                          Datum: {new Date(obavijest.datumObavijest).toLocaleString("hr-HR")}
                        </div>
                      </div>
                  ))
              ) : (
                  <p>Nema obavijesti za prikaz.</p>
              )}
            </div>

            <WeatherWidger />

          </div>
      );
    }


    if (activeSection === "Predmeti") {
      if (selectedSubject) {
        return (
            <div>
              <nav className="subject-navbar">
                <button
                    className={`nav-btn ${activeTab === "Materijali" ? "active" : ""}`}
                    onClick={() => setActiveTab("Materijali")}
                >
                  Materijali
                </button>
                <button
                    className={`nav-btn ${activeTab === "Obavijesti" ? "active" : ""}`}
                    onClick={() => setActiveTab("Obavijesti")}
                >
                  Obavijesti
                </button>
              </nav>
              {activeTab === "Materijali" && renderMaterials()}
              {activeTab === "Obavijesti" && (
                  <div>
                    {renderObavijestiList()} {/* Prikaz liste obavijesti */}
                  </div>
              )}



            </div>
        );
      }
      return subjects.length > 0 ? renderSubjects() : <h4>Učitavanje predmeta...</h4>;
    }

    if (activeSection === "Aktivnosti") {
      return (
          <div>
            {userEmail ? (
                <UcenikAktivnosti userEmail={userEmail} />
            ) : (
                <p>Loading aktivnosti...</p>
            )}
          </div>
      );
    }

    if (activeSection === "Obavijesti") {
      return (
          <div>
            <h3>Opće Obavijesti</h3>
            {renderObavijestiList()}
          </div>
      );
    }

    if (activeSection === "Raspored") {
      return (
          <div className="content-container">
            {userEmail ? <Timetable email={userEmail} /> : <p>Loading timetable...</p>}
          </div>
      );
    }

    if (activeSection === "Potvrde") {
      return (
          <div className="content-container">
            {userEmail ? <UcenikPotvrde userEmail={userEmail} /> : <p>Loading...</p>}
          </div>
      );
    }

    if (activeSection === "Chat") {
      return (
          <div>
            return <Navigate to="/chat2" />;
          </div>
      );
    }

    if (activeSection === "Karta") {
      return <MapRoute />;
    }
    //

    return <h4>Odaberite sekciju iz izbornika.</h4>;
  };

  const menuItems = [
    { name: "Naslovnica", icon: <FaBook /> },
    { name: "Predmeti", icon: <FaBook /> },
    { name: "Aktivnosti", icon: <FaTasks /> },
    { name: "Raspored", icon: <FaCalendarAlt /> },
    { name: "Obavijesti", icon: <FaEnvelope /> },
    { name: "Potvrde", icon: <FaEnvelope /> },
    { name: "Chat", icon: <FaCommentDots /> },
    { name: "Karta", icon: <FaMap /> }
  ];

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

export default Ucenik;

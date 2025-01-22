import React, { useState, useEffect } from "react";
import { FaChartBar, FaChalkboard, FaComments } from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import TableUceniciNastavnik from "../../components/TableUceniciNastavnik";
import Timetable from "../../components/Timetable";
import WeatherWidget from "../../components/WeatherWidget";
import "./Nastavnik.css";
import {Navigate} from "react-router-dom";

const Nastavnik = () => {
  const [activeSection, setActiveSection] = useState("Naslovnica");
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState(null);
  const [activeTab, setActiveTab] = useState("Materijali");
  const [materials, setMaterials] = useState([]);
  const [isDeleting, setIsDeleting] = useState(false);
  const [viewingReport, setViewingReport] = useState(false);
  const [reportData, setReportData] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null); // Email učenika
  const [selectedStudentSubject, setSelectedStudentSubject] = useState(null); // Naziv predmeta
  const [userEmail, setUserEmail] = useState(null);
  const [obavijestNaslov, setObavijestNaslov] = useState("");
  const [obavijestSadrzaj, setObavijestSadrzaj] = useState("");
  const [obavijesti, setObavijesti] = useState([]); // Lista obavijesti
const [isDeletingObavijesti, setIsDeletingObavijesti] = useState(false); // Režim brisanja
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


useEffect(() => {
  if (activeTab === "Obavijesti" && selectedSubject) {
    fetchObavijesti();
  }
}, [activeTab, selectedSubject]);



  const fetchObavijesti = async () => {
    if (!selectedSubject) {
      console.error("Predmet nije odabran.");
      return;
    }
  
    try {
      const response = await fetch(`/api/nastavnik/obavijesti?nazPredmet=${selectedSubject}`, {
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
  
  
  const handleDeleteObavijest = async (sifObavijest) => {
    try {
      const response = await fetch(`/api/nastavnik/obavijesti?sifObavijest=${sifObavijest}`, {
        method: "DELETE",
      });
      if (response.ok) {
        alert("Obavijest uspješno obrisana!");
        fetchObavijesti(); // Osvježi listu nakon brisanja
      } else {
        alert("Greška prilikom brisanja obavijesti.");
      }
    } catch (error) {
      console.error("Greška pri brisanju obavijesti:", error);
    }
  };
  
  

  const handleSendObavijest = async () => {
    if (!obavijestNaslov || !obavijestSadrzaj || !selectedSubject) {
      alert("Molimo unesite sve podatke!");
      return;
    }
  
    // Kreiranje podataka u formatu `application/x-www-form-urlencoded`
    const formData = new URLSearchParams();
    formData.append("naslovObavijest", obavijestNaslov);
    formData.append("sadrzajObavijest", obavijestSadrzaj);
    formData.append("nazPredmet", selectedSubject);
  
    try {
      const response = await fetch("/api/nastavnik/obavijesti", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded", // Ključno za @RequestParam
        },
        body: formData.toString(),
      });
  
      if (response.ok) {
        alert("Obavijest uspješno poslana!");
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


 const renderObavijestiList = () => (
  <div className="obavijesti-list">
    <h3>Popis obavijesti</h3>
    {obavijesti.length > 0 ? (
      <div>
        {obavijesti.map((obavijest) => (
          <div key={obavijest.sifObavijest} className="obavijest-item">
            <strong>{obavijest.naslovObavijest}</strong>
            <div>{obavijest.sadrzajObavijest}</div>
            <div className="obavijest-datum">
              Datum: {new Date(obavijest.datumObavijest).toLocaleString("hr-HR")}
            </div>
            {isDeletingObavijesti && (
              <button
                className="delete-obavijest-btn"
                onClick={() => handleDeleteObavijest(obavijest.sifObavijest)}
              >
                🗑️
              </button>
            )}
          </div>
        ))}
      </div>
    ) : (
      <p>Nema obavijesti za prikaz.</p>
    )}
    <div className="delete-toggle-container">
      {!isDeletingObavijesti ? (
        <button
          onClick={() => setIsDeletingObavijesti(true)}
          className="delete-toggle-btn"
        >
          Obriši obavijesti
        </button>
      ) : (
        <button
          onClick={() => setIsDeletingObavijesti(false)}
          className="delete-toggle-btn"
        >
          Gotovo
        </button>
      )}
    </div>
  </div>
);

  

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
      disabled={!obavijestNaslov || !obavijestSadrzaj || !selectedSubject}
    >
      Pošalji obavijest
    </button>
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
  
  // Fetch za izvještaj
  const fetchReport = async () => {
    try {
      const response = await fetch("/api/nastavnik/materijali/izvjestaj", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Greška prilikom dohvaćanja izvještaja.");
      const data = await response.json();
      setReportData(data);
      setViewingReport(true);
    } catch (error) {
      console.error("Greška pri dohvaćanju izvještaja:", error);
    }
  };

  // Fetch za materijale
  const fetchMaterials = async () => {
    try {
      const response = await fetch(`/api/nastavnik/materijali?predmet=${selectedSubject}`, {
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
      const response = await fetch("/api/nastavnik/predmeti", {
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

  const handleAddMaterial = () => {
    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.onchange = async (event) => {
      const file = event.target.files[0];
      if (!file || !selectedSubject) return;

      const formData = new FormData();
      formData.append("file", file);
      formData.append("predmet", selectedSubject);

      try {
        const response = await fetch("/api/nastavnik/materijali", {
          method: "POST",
          body: formData,
        });
        if (response.ok) {
          alert("Materijal uspješno dodan!");
          fetchMaterials();
        } else {
          alert("Greška pri dodavanju materijala.");
        }
      } catch (error) {
        console.error("Greška pri dodavanju materijala:", error);
      }
    };
    fileInput.click();
  };

  const handleDeleteMaterial = async (material) => {
    try {
      const response = await fetch(`/api/nastavnik/materijali?key=${material}`, {
        method: "DELETE",
      });
      if (response.ok) {
        alert("Materijal uspješno obrisan!");
        fetchMaterials();
      } else {
        alert("Greška pri brisanju materijala.");
      }
    } catch (error) {
      console.error("Greška pri brisanju materijala:", error);
    }
  };

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

  const renderReport = () => {
    const filteredReport = reportData.filter(
      (entry) =>
        entry.email === selectedStudent &&
        entry.iemPredmet === selectedStudentSubject
    );
  
    return (
      <div className="report-container">
        <h2>Izvještaj preuzetih materijala</h2>
        {filteredReport.length > 0 ? (
          <table className="report-table">
            <thead>
              <tr>
                <th>Ime učenika</th>
                <th>Prezime učenika</th>
                <th>Email</th>
                <th>Datum preuzimanja</th>
                <th>Materijal</th>
                <th>Predmet</th>
              </tr>
            </thead>
            <tbody>
              {filteredReport.map((entry, index) => (
                <tr key={index}>
                  <td>{entry.imeUcenik}</td>
                  <td>{entry.prezimeUcenik}</td>
                  <td>{entry.email}</td>
                  <td>{new Date(entry.datum).toLocaleString("hr-HR")}</td>
                  <td>
                    <a
                      href={`https://eduhub-materials.s3.amazonaws.com/${entry.imeMaterijal}`}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      {entry.imeMaterijal.split("_").pop()}
                    </a>
                  </td>
                  <td>{entry.iemPredmet.replace(/_/g, " ")}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>Nema preuzetih materijala za ovog učenika na ovom predmetu.</p>
        )}
        <button onClick={() => setViewingReport(false)} className="back-btn">
          Povratak
        </button>
      </div>
    );
  };
  
  
  

  const renderMaterials = () => (
    <div className="materials-grid">
      <button onClick={handleAddMaterial} className="add-material-btn">
        Dodaj materijal
      </button>
      {!isDeleting ? (
        <button
          className="delete-materials-btn"
          onClick={() => setIsDeleting(true)}
        >
          Obriši materijale
        </button>
      ) : (
        <button
          className="done-btn"
          onClick={() => setIsDeleting(false)}
        >
          Gotovo
        </button>
      )}
      {materials.length > 0 ? (
        materials.map((material) => (
          <div key={material} className="material-card">
            {isDeleting && (
              <button
                className="delete-icon-material"
                onClick={() => handleDeleteMaterial(material)}
              >
                🗑️
              </button>
            )}
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
    if (viewingReport) {
      return renderReport();
    }

    if (activeSection === "Naslovnica") {
      return (
        <div>
            <h1>Pozdrav, {userName}! </h1>
             <WeatherWidget />
        </div>
      );
    }

    if (activeSection === "Predmeti") {
      if (selectedSubject) {
        return (
          <div>
            <nav className="subject-navbar">
              <button
                className={`nav-btn ${activeTab === "Učenici" ? "active" : ""}`}
                onClick={() => setActiveTab("Učenici")}
              >
                Učenici
              </button>
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
            {activeTab === "Učenici" && (
              <TableUceniciNastavnik
                selectedSubject={selectedSubject}
                fetchReport={fetchReport}
                setSelectedStudent={setSelectedStudent} // Prosljeđivanje funkcije
                setSelectedStudentSubject={setSelectedStudentSubject}
              />
            )}
            {activeTab === "Materijali" && renderMaterials()}
            {activeTab === "Obavijesti" && (
            <div>
              {renderObavijestiForm()} {/* Prikaz forme za dodavanje */}
              {renderObavijestiList()} {/* Prikaz liste obavijesti */}
            </div>
          )}
           

          
          </div>
        );
      }
      return subjects.length > 0 ? renderSubjects() : <h4>Učitavanje predmeta...</h4>;
    }

    if (activeSection === "Raspored") {
      
                return (
                    <div className="content-container">
                        {userEmail ? <Timetable email={userEmail} /> : <p>Loading timetable...</p>}
                    </div>
                );
      }

    if (activeSection === "Chat") {
      return (
        <div>
          <Navigate to="/chat" />;
        </div>
      );
    }

    return <h4>Odaberite sekciju iz izbornika.</h4>;
  };

  const menuItems = [
    { name: "Naslovnica", icon: <FaChartBar /> },
    { name: "Predmeti", icon: <FaChartBar /> },
    { name: "Raspored", icon: <FaChalkboard /> },
    { name: "Chat", icon: <FaComments /> },
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

export default Nastavnik;
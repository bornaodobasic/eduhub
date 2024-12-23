import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import './Admin.css';

const Admin = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [activeRole, setActiveRole] = useState("");
    const [activeSidebarOption, setActiveSidebarOption] = useState("");
    const [leftSidebarOptions, setLeftSidebarOptions] = useState([]);
    const [mainContent, setMainContent] = useState("");

    const roles = ["ucenik", "nastavnik", "ravnatelj", "satnicar", "djelatnik", "admin"]

    /* dohvat korisnika */
    const fetchUcenici = async () => {
        try {
            const response = await fetch('/admin/ucenik');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja učenika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih učenika</h3>
                    <ul>
                        {data.map((ucenik) => (
                            <li key={ucenik.id}>
                                {ucenik.imeUcenik} {ucenik.prezimeUcenik} - {ucenik.email}
                                <button className="delete-button" onClick={() => handleDelete("ucenik",  ucenik.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };

    const fetchNastavnici = async () => {
        try {
            const response = await fetch('/admin/nastavnik');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja nastavnika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih nastavnika</h3>
                    <ul>
                        {data.map((nastavnik) => (
                            <li key={nastavnik.id}>
                                {nastavnik.imeNastavnik} {nastavnik.prezimeNastavnik} - {nastavnik.email}
                                <button className="delete-button" onClick={() => handleDelete("nastavnik",  nastavnik.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };

    const fetchRavnatelji = async () => {
        try {
            const response = await fetch('/admin/ravnatelj');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja nastavnika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih ravnatelja</h3>
                    <ul>
                        {data.map((ravnatelj) => (
                            <li key={ravnatelj.id}>
                                {ravnatelj.imeRavnatelj} {ravnatelj.prezimeRavnatelj} - {ravnatelj.email}
                                <button className="delete-button" onClick={() => handleDelete("ravnatelj", ravnatelj.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };

    const fetcHSatnicari = async () => {
        try {
            const response = await fetch('/admin/satnicar');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja nastavnika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih satničara</h3>
                    <ul>
                        {data.map((satnicar) => (
                            <li key={satnicar.id}>
                                {satnicar.imeSatnicar} {satnicar.prezimeSatnicar} - {satnicar.email}
                                <button className="delete-button" onClick={() => handleDelete("satnicar", satnicar.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };

    const fetchcDjelatnici = async () => {
        try {
            const response = await fetch('/admin/djelatnik');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja nastavnika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih djelatnika</h3>
                    <ul>
                        {data.map((djelatnik) => (
                            <li key={djelatnik.id}>
                                {djelatnik.imeDjel} {djelatnik.prezimeDjel} - {djelatnik.email}
                                <button className="delete-button" onClick={() => handleDelete("djelatnik", djelatnik.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };

    const fetchAdmini = async () => {
        try {
            const response = await fetch('/admin/admin');
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja nastavnika.");
            }
            const data = await response.json();
            setMainContent(
                <div>
                    <h3>Popis svih admina</h3>
                    <ul>
                        {data.map((admin) => (
                            <li key={admin.id}>
                                {admin.imeAdmin} {admin.prezimeAdmin} - {admin.email}
                                <button className="delete-button" onClick={() => handleDelete("admin", admin.email)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(
                <p>Došlo je do greške prilikom dohvaćanja podataka: {error.message}</p>
            );
        }
    };


    /* dodavanje korsnika */
    const handleUcenikSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            imeUcenik: formData.get("imeUcenik"),
            prezimeUcenik: formData.get("prezimeUcenik"),
            spol: formData.get("spol"),
            razred: formData.get("razred"),
            datumRodenja: formData.get("datumRodenja"),
            oib: formData.get("oib"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/ucenik/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Učenik uspješno dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati učenika'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };

    const handleNastavnikSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/nastavnik/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Nastavnik dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati nastavnika'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };

    const handleRavnateljSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/ravnatelj/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Ravnatelj dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati ravnatelja'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };

    const handleDjelatnikSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/djelatnik/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Djelatnik dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati djelatnika'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };

    const handleSatnicarSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/satnicar/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Satničar dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati satničara'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };

    const handleAdminSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch('/admin/admin/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Admin dodan!");
            } else {
                const error = await response.json();
                alert(`Greška: ${error.message || 'Nije moguće dodati admina'}`);
            }
        } catch (error) {
            alert(`Greška pri slanju zahtjeva: ${error.message}`);
        }
    };


    /* brisanje korisnika */
    const handleDelete = async (role, email) => {
        const confirmDelete = window.confirm(`Jeste li sigurni da želite obrisati ${role} s emailom: ${email}?`);
        if (!confirmDelete) return;

        try {
            const response = await fetch(`/admin/${role}/delete/${email}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                throw new Error(`Greška prilikom brisanja ${role} s emailom: ${email}`);
            }

            alert(`${role.charAt(0).toUpperCase() + role.slice(1)} s emailom ${email} je obrisan.`);

            switch(role){
                case "ucenik":
                    fetchUcenici();
                    break;
                case "nastavnik":
                    fetchNastavnici();
                    break;
                case "ravnatelj":
                    fetchRavnatelji();
                    break;
                case "satnicar":
                    fetcHSatnicari();
                    break;
                case "djelatnik":
                    fetchcDjelatnici();
                    break;
                case "admin":
                    fetchAdmini();
                    break;
            }



        } catch (error) {
            alert(`Došlo je do greške: ${error.message}`);
        }
    };


    useEffect(() => {
        const role = location.pathname.split("/").pop();
        setActiveRole(role);
        setActiveSidebarOption("");

        switch (role) {
            case "ucenik":
                setLeftSidebarOptions(["Dodaj učenika", "Pregled učenika", "Funcija3 učenik"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            case "nastavnik":
                setLeftSidebarOptions(["Dodaj nastavnika", "Pregled nastavnika", "Predmeti nastavnika"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            case "ravnatelj":
                setLeftSidebarOptions(["Dodaj ravnatelja", "Pregled ravnatelja"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            case "satnicar":
                setLeftSidebarOptions(["Dodaj satničara", "Pregled satničara", "Funckija3 satničar"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            case "djelatnik":
                setLeftSidebarOptions(["Dodaj djelatnika", "Pregled djelatnika"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            case "admin":
                setLeftSidebarOptions(["Dodaj admina", "Pregled admina"]);
                setMainContent("Odaberite opciju iz lijevog izbornika.");
                break;
            default:
                setLeftSidebarOptions([]);
                setMainContent("Odaberite ulogu iz gornjeg izbornika.");
        }
    }, [location]);

    useEffect(() => {
        switch (activeSidebarOption) {
            case "Dodaj učenika":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj učenikaaaa</h4>
                        <form onSubmit={handleUcenikSubmit} >
                            <input type="text" name="imeUcenik" placeholder="Ime učenika" required />
                            <input type="text" name="prezimeUcenik" placeholder="Prezime učenika" required />
                            <input type="text" name="spol" placeholder="Spol (M/Ž)" required  />
                            <input type="text"name="razred" placeholder="Razred" required />
                            <input type="date" name="datumRodenja" required/>
                            <input type="text" name="oib" placeholder="OIB" />
                            <input type="text" name="email" placeholder="Email učenika" required/>

                            <button type="submit">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled učenika":
                fetchUcenici();
                break;

            case "Dodaj nastavnika":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj nastavnika:</h4>
                        <form onSubmit={handleNastavnikSubmit} className="user-form">

                            <input type="text" name="ime" placeholder="Ime nastavnika" className="add-input" />
                            <input type="text" name="prezime" placeholder="Prezime nastavnika" className="add-input" />
                            <input type="email" name="email" placeholder="Email nastavnika" className="add-input" />
                            <button type="submit" className="add-button">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled nastavnika":
                fetchNastavnici();
                break;

            case "Predmeti nastavnika":
                const fetchPredmetiNastavnika = async () => {
                    try {
                        const response = await fetch('/admin/nastavnik'); // Dohvati sve nastavnike
                        if (!response.ok) {
                            throw new Error("Greška prilikom dohvaćanja nastavnika.");
                        }
                        const nastavnici = await response.json();

                        setMainContent(
                            <div>
                                <h3>Predmeti nastavnika</h3>
                                <ul>
                                    {nastavnici.map((nastavnik) => (
                                        <li key={nastavnik.email}>
                                            <b>{nastavnik.imeNastavnik} {nastavnik.prezimeNastavnik}</b>
                                            <button
                                                onClick={() => handlePregledPredmeta(nastavnik.email)}
                                                className="view-button"
                                            >
                                                Pregledaj predmete
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        );
                    } catch (error) {
                        setMainContent(<p>Došlo je do greške: {error.message}</p>);
                    }
                };

                fetchPredmetiNastavnika();
                break;


            case "Dodaj ravnatelja":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj ravnatelja:</h4>
                        <form onSubmit={handleRavnateljSubmit} className="user-form">

                            <input type="text" name="ime" placeholder="Ime ravnatelja" className="add-input" />
                            <input type="text" name="prezime" placeholder="Prezime ravnatelja" className="add-input" />
                            <input type="email" name="email" placeholder="Email ravnatelja" className="add-input" />
                            <button type="submit" className="add-button">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled ravnatelja":
                fetchRavnatelji();
                break;

            case "Dodaj djelatnika":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj djelatnika:</h4>
                        <form onSubmit={handleDjelatnikSubmit} className="user-form">

                            <input type="text" name="ime" placeholder="Ime djelatnika" className="add-input" />
                            <input type="text" name="prezime" placeholder="Prezime djelatnika" className="add-input" />
                            <input type="email" name="email" placeholder="Email djelatnika" className="add-input" />
                            <button type="submit" className="add-button">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled djelatnika":
                fetchcDjelatnici();
                break;

            case "Dodaj satničara":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj satničara:</h4>
                        <form onSubmit={handleSatnicarSubmit} className="user-form">

                            <input type="text" name="ime" placeholder="Ime satničara" className="add-input" />
                            <input type="text" name="prezime" placeholder="Prezime satničara" className="add-input" />
                            <input type="email" name="email" placeholder="Email satničara" className="add-input" />
                            <button type="submit" className="add-button">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled satničara":
                fetcHSatnicari();
                break;

            case "Dodaj admina":
                setMainContent(
                    <div className="add">
                        <h4 className="add-title">Dodaj admina:</h4>
                        <form onSubmit={handleAdminSubmit} className="user-form">

                            <input type="text" name="ime" placeholder="Ime admina" className="add-input" />
                            <input type="text" name="prezime" placeholder="Prezime admina" className="add-input" />
                            <input type="email" name="email" placeholder="Email admina" className="add-input" />
                            <button type="submit" className="add-button">DODAJ</button>
                        </form>
                    </div>
                );
                break;

            case "Pregled admina":
                fetchAdmini();
                break;

            default:
                setMainContent("Odaberite opciju iz lijevog izbornika.");
        }
    }, [activeSidebarOption]);

    const handlePregledPredmeta = async (email) => {
        try {
            const response = await fetch(`/admin/nastavnik/predmeti/${email}`);
            if (!response.ok) {
                throw new Error("Greška prilikom dohvaćanja predmeta nastavnika.");
            }
            const predmeti = await response.json();

            setMainContent(
                <div>
                    <h3>Predmeti koje predaje nastavnik ({email}):</h3>
                    <ul>
                        {predmeti.map((predmet) => (
                            <li key={predmet.sifPredmet}>
                                {predmet.nazPredmet} - {predmet.ukBrSatiTjedno} sati tjedno
                            </li>
                        ))}
                    </ul>
                </div>
            );
        } catch (error) {
            setMainContent(<p>Došlo je do greške: {error.message}</p>);
        }
    };


    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container-admin">

                <aside className="sidebar-left-admin">
                    {leftSidebarOptions.map((option, index) => (
                        <button
                            key={index}
                            className={`sidebar-button-admin ${activeSidebarOption === option ? "active" : ""}`}
                            onClick={() => setActiveSidebarOption(option)}
                        >
                            {option}
                        </button>
                    ))}
                </aside>

                <div className="main-content-admin">
                    <div className="izbornik">
                        {roles.map((role) => (
                            <button
                                key={role}
                                className={`izborButton ${activeRole === role ? "active" : ""}`}
                                onClick={() => navigate(`/admin/${role}`)}
                            >
                                {role.toUpperCase()}
                            </button>
                        ))}
                    </div>

                    <div className="content">{mainContent}</div>
                </div>

                <aside className="sidebar-right-admin">
                    <WeatherWidget></WeatherWidget>
                </aside>
            </div>
        </div>
    );
};

export default Admin;
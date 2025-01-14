import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Header from "../../components/Header";
import WeatherWidget from "../../components/WeatherWidget";
import "./Admin.css";

const Admin = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [mainContent, setMainContent] = useState(null);
    const [data, setData] = useState([]);
    const [selectedRole, setSelectedRole] = useState("");
    const [sidebarContent, setSidebarContent] = useState(null);

    const roles = ["ucenik", "nastavnik", "ravnatelj", "satnicar", "djelatnik", "admin"];

    useEffect(() => {
        const role = location.pathname.split("/").pop();
        setSelectedRole(role);
        fetchRoleData(role);
    }, [location]);

    const fetchRoleData = async (role) => {
        try {
            const response = await fetch(`/api/admin/${role}`);
            if (!response.ok) throw new Error("Greška prilikom dohvaćanja podataka.");

            const roleData = await response.json();
            roleData.sort((a, b) => (a.ime || "").localeCompare(b.ime || ""));
            setData(roleData);

            setMainContent(
                <div className="main-content-center">
                    <h3>Popis svih {role}a</h3>
                    <div className="table-container">
                        <table className="styled-table">
                            <thead>
                                <tr>
                                    <th>Ime i Prezime</th>
                                    <th>Email</th>
                                    <th>Akcija</th>
                                </tr>
                            </thead>
                            <tbody>
                                {roleData.map((item) => (
                                    <tr key={item.id}>
                                        <td>{`${item.ime || item.imeUcenik || item.imeNastavnik || item.imeRavnatelj || item.imeDjel || item.imeSatnicar} ${item.prezime || item.prezimeUcenik || item.prezimeNastavnik || item.prezimeRavnatelj || item.prezimeDjel || item.prezimeSatnicar}`}</td>
                                        <td>{item.email}</td>
                                        <td>
                                            <button onClick={() => handleDelete(role, item.email)}>Obriši</button>
                                            <button onClick={() => handleViewDetails(role, item.email)}>Pregled</button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            );

            setSidebarContent(
                role === "ucenik" ? <UcenikForm /> : <RoleForm role={role} />
            );
        } catch (error) {
            setMainContent(<p>Greška: {error.message}</p>);
        }
    };

    const handleDelete = async (role, email) => {
        if (!window.confirm(`Jeste li sigurni da želite obrisati ${role} s emailom: ${email}?`)) return;

        try {
            const response = await fetch(`/api/admin/${role}/delete/${email}`, { method: "DELETE" });
            if (!response.ok) throw new Error("Greška prilikom brisanja korisnika.");

            alert(`${role.charAt(0).toUpperCase() + role.slice(1)} obrisan.`);
            fetchRoleData(selectedRole);
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

    const handleViewDetails = async (role, email) => {
        try {
            const endpoint = role === "ucenik" ? `/api/admin/ucenik/aktivnosti/je/${email}` : `/api/admin/nastavnik/predmeti/predaje/${email}`;
            const response = await fetch(endpoint);
            if (!response.ok) throw new Error("Greška prilikom dohvaćanja detalja.");

            const details = await response.json();
            details.sort((a, b) => ((a.nazPredmet || a.oznAktivnost || "").localeCompare(b.nazPredmet || b.oznAktivnost || "")));

            setMainContent(
                <div className="main-content-center">
                    <h4>{role === "ucenik" ? `Popis aktivnosti učenika (${email})` : `Popis predmeta nastavnika (${email})`}</h4>
                    <div className="table-container">
                        <table className="styled-table">
                            <thead>
                                <tr>
                                    <th>Naziv</th>
                                    <th>Akcija</th>
                                </tr>
                            </thead>
                            <tbody>
                                {details.map((detail) => (
                                    <tr key={detail.sifPredmet || detail.sifAktivnost}>
                                        <td>{detail.nazPredmet || detail.oznAktivnost}</td>
                                        <td>
                                            <button onClick={() => handleDeleteDetail(role, email, detail.nazPredmet || detail.oznAktivnost)}>
                                                Obriši
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            );

            const unusedEndpoint = role === "ucenik" ? `/api/admin/ucenik/aktivnosti/nije/${email}` : `/api/admin/nastavnik/predmeti/nepredaje/${email}`;
            const unusedResponse = await fetch(unusedEndpoint);
            if (!unusedResponse.ok) throw new Error("Greška prilikom dohvaćanja dostupnih podataka.");

            const unusedItems = await unusedResponse.json();
            unusedItems.sort((a, b) => (a.nazPredmet || a.oznAktivnost || "").localeCompare(b.nazPredmet || b.oznAktivnost || ""));

            setSidebarContent(
                role === "ucenik" ? (
                    <ActivityDropdownForm email={email} fetchDetails={() => handleViewDetails(role, email)} unusedItems={unusedItems} />
                ) : (
                    <SubjectDropdownForm email={email} fetchDetails={() => handleViewDetails(role, email)} unusedItems={unusedItems} />
                )
            );
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

    const handleDeleteDetail = async (role, email, detailName) => {
        try {
            const endpoint = role === "ucenik" ? `/api/admin/ucenik/aktivnosti/delete/${email}` : `/api/admin/nastavnik/predmeti/delete/${email}`;
            const response = await fetch(endpoint, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify([detailName]),
            });

            if (!response.ok) throw new Error("Greška prilikom brisanja detalja.");

            alert("Detalj obrisan.");
            handleViewDetails(role, email);
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };


    const ActivityDropdownForm = ({ email, fetchDetails, unusedItems }) => {
        const [selected, setSelected] = useState([]);
    
        const handleCheckboxChange = (item) => {
            setSelected((prev) =>
                prev.includes(item.oznAktivnost)
                    ? prev.filter((i) => i !== item.oznAktivnost) // Makni ako je već odabrano
                    : [...prev, item.oznAktivnost] // Dodaj ako nije odabrano
            );
        };
    
        const handleAdd = async () => {
            if (selected.length === 0) {
                alert("Odaberite barem jednu aktivnost prije dodavanja.");
                return;
            }
    
            try {
                const response = await fetch(`/api/admin/ucenik/aktivnosti/add/${email}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(selected),
                });
    
                if (!response.ok) throw new Error("Greška prilikom dodavanja aktivnosti.");
    
                alert("Aktivnosti dodane.");
                fetchDetails();
                setSelected([]); // Resetiraj odabir
            } catch (error) {
                alert(`Greška: ${error.message}`);
            }
        };
    
        return (
            <div className="checkbox-form">
                <ul className="checkbox-list">
                    {unusedItems.map((item) => (
                        <li key={item.sifAktivnost} className="checkbox-item">
                            <label>
                                <input
                                    type="checkbox"
                                    value={item.oznAktivnost}
                                    checked={selected.includes(item.oznAktivnost)}
                                    onChange={() => handleCheckboxChange(item)}
                                />
                                {item.oznAktivnost}
                            </label>
                        </li>
                    ))}
                </ul>
                <button onClick={handleAdd}>Dodaj</button>
            </div>
        );
    };
    
    
  
    const SubjectDropdownForm = ({ email, fetchDetails, unusedItems }) => {
        const [selected, setSelected] = useState([]);
    
        const handleCheckboxChange = (item) => {
            setSelected((prev) =>
                prev.includes(item.nazPredmet)
                    ? prev.filter((i) => i !== item.nazPredmet) // Makni ako je već odabrano
                    : [...prev, item.nazPredmet] // Dodaj ako nije odabrano
            );
        };
    
        const handleAdd = async () => {
            if (selected.length === 0) {
                alert("Odaberite barem jedan predmet prije dodavanja.");
                return;
            }
    
            try {
                const response = await fetch(`/api/admin/nastavnik/predmeti/add/${email}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(selected),
                });
    
                if (!response.ok) throw new Error("Greška prilikom dodavanja predmeta.");
    
                alert("Predmeti dodani.");
                fetchDetails();
                setSelected([]); // Resetiraj odabir
            } catch (error) {
                alert(`Greška: ${error.message}`);
            }
        };
    
        return (
            <div className="checkbox-form">
                <ul className="checkbox-list">
                    {unusedItems.map((item) => (
                        <li key={item.sifPredmet} className="checkbox-item">
                            <label>
                                <input
                                    type="checkbox"
                                    value={item.nazPredmet}
                                    checked={selected.includes(item.nazPredmet)}
                                    onChange={() => handleCheckboxChange(item)}
                                />
                                {item.nazPredmet}
                            </label>
                        </li>
                    ))}
                </ul>
                <button onClick={handleAdd}>Dodaj</button>
            </div>
        );
    };
        

    const UcenikForm = () => {
        const handleSubmit = async (e) => {
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
                const response = await fetch(`/api/admin/ucenik/add`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data),
                });

                if (!response.ok) throw new Error("Greška prilikom dodavanja učenika.");

                alert("Učenik dodan.");
                fetchRoleData("ucenik");
            } catch (error) {
                alert(`Greška: ${error.message}`);
            }
        };

        return (
            <form onSubmit={handleSubmit} className="sidebar-form">
                <input type="text" name="imeUcenik" placeholder="Ime učenika" required />
                <input type="text" name="prezimeUcenik" placeholder="Prezime učenika" required />
                <input type="text" name="spol" placeholder="Spol (M/Ž)" required />
                <input type="text" name="razred" placeholder="Razred" required />
                <input type="date" name="datumRodenja" required />
                <input type="text" name="oib" placeholder="OIB" required />
                <input type="email" name="email" placeholder="Email učenika" required />
                <button type="submit">Dodaj učenika</button>
            </form>
        );
    };

    const RoleForm = ({ role }) => {
        const handleSubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);

            const data = {
                ime: formData.get("ime"),
                prezime: formData.get("prezime"),
                email: formData.get("email"),
            };

            try {
                const response = await fetch(`/api/admin/${role}/add`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data),
                });

                if (!response.ok) throw new Error("Greška prilikom dodavanja korisnika.");

                alert("Korisnik dodan.");
                fetchRoleData(role);
            } catch (error) {
                alert(`Greška: ${error.message}`);
            }
        };

        return (
            <form onSubmit={handleSubmit} className="sidebar-form">
                <input type="text" name="ime" placeholder="Ime" required />
                <input type="text" name="prezime" placeholder="Prezime" required />
                <input type="email" name="email" placeholder="Email" required />
                <button type="submit">Dodaj</button>
            </form>
        );
    };

    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container-admin">
                <aside className="sidebar-left-admin">
                    {roles.map((role) => (
                        <button
                            key={role}
                            className={`sidebar-button-admin ${selectedRole === role ? "active" : ""}`}
                            onClick={() => navigate(`/admin/${role}`)}
                        >
                            {role.toUpperCase()}
                        </button>
                    ))}
                </aside>

                <div className="main-content-admin">
                    {mainContent}
                </div>

                <aside className="sidebar-right-admin">
                    {sidebarContent}
                    <WeatherWidget />
                </aside>
            </div>
        </div>
    );
};

export default Admin;

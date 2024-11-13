import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import './Enroll.css';

const Enroll = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const queryParams = new URLSearchParams(location.search);

    const [formData, setFormData] = useState({
        ime: "",
        prezime: "",
        oib: "",
        spol: "",
        datumRodenja: "",
        email: "",
        lozinka: ""
    });

    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formDataUcenik = {
            imeUcenik: formData.ime,
            prezimeUcenik: formData.prezime,
            oib: formData.oib,
            spol: formData.spol,
            datumRodenja: formData.datumRodenja,
            email: formData.email,
            lozinka: formData.lozinka
        };

        
            const response = await fetch("http://localhost:8080/upis", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formDataUcenik),
            });

            const data = await response.text(); // Pošto backend vraća plain text, koristimo `text()`

            if (response.ok) {
                setMessage("Zahtjev uspješno poslan adminu."); // Poruka ako je uspješno poslano
                navigate("/login");
            } else {
                setMessage(data || "Došlo je do greške.");
            }
        
    };

    return (
        <div className="outer-enroll-container">
            <div className="enroll-container">
                <form className="login-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <input
                            type="text"
                            name="ime"
                            placeholder="Ime"
                            value={formData.ime}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="prezime"
                            placeholder="Prezime"
                            value={formData.prezime}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="oib"
                            placeholder="OIB"
                            value={formData.oib}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="spol"
                            placeholder="Spol"
                            value={formData.spol}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="datumRodenja"
                            placeholder="Datum rođenja"
                            value={formData.datumRodenja}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="text"
                            name="email"
                            placeholder="Email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="password"
                            name="lozinka"
                            placeholder="Lozinka"
                            value={formData.lozinka}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <select required defaultValue="">
                            <option value="" disabled>Odaberi željeni smjer</option>
                            <option value="opca">Opća gimnazija</option>
                            <option value="prirodoslovno-matematicka">Prirodoslovno-matematička gimnazija</option>
                            <option value="jezicna">Jezična gimnazija</option>
                        </select>
                    </div>
                    <button type="submit">POTVRDI</button>
                </form>
                {message && <p>{message}</p>}
            </div>
        </div>
    );
};

export default Enroll;

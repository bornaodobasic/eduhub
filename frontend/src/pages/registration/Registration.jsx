import React, { useState } from "react";
import {useLocation, useNavigate} from "react-router-dom"

const Registration = () => {
    const location=useLocation()
    const navigate = useNavigate();
    const queryParams=new URLSearchParams(location.search)

    const [formData, setFormData] = useState({
        ime: "",
        prezime: "",
        email: "",
        lozinka: "",
    });

    const [message, setMessage] = useState(""); // Poruka nakon registracije

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        
        e.preventDefault();
        let role=queryParams.get("role")
        
        
            let formDataKorisnik={
                imeKorisnik:formData.ime,
                prezimeKorisnik:formData.prezime,
                email:formData.email,
                lozinka:formData.lozinka,
                role:role
            }

            try {
                
                const response = await fetch("http://localhost:8080/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(formDataKorisnik),
                });
    
                const data = await response.json();
                if (response.ok) {
                    setMessage(data.message); // Ako je registracija uspešna
                    navigate("/login");
                } else {
                    setMessage(data.message || "Došlo je do greške.");
                }
            } catch (error) {
                setMessage("Došlo je do greške u komunikaciji sa serverom.");
                navigate("/login");
            }
        
        
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input
                        type="text"
                        name="ime"
                        placeholder="ime"
                        value={formData.ime}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <input
                        type="text"
                        name="prezime"
                        placeholder="prezime"
                        value={formData.prezime}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <input
                        type="email"
                        name="email"
                        placeholder="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <input
                        type="password"
                        name="lozinka"
                        placeholder="lozinka"
                        value={formData.lozinka}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">REGISTRIRAJ SE</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Registration;

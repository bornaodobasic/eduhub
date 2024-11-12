import React, { useState } from "react";
import {useLocation, useNavigate} from "react-router-dom"

const Enroll = () => {
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
        <div className="outer-enroll-container">
            <div className="enroll-container">
                <form className="login-form">
                    <div className="form-group">
                        <input type="text" placeholder="ime" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="prezime" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="OIB" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="datum rodenja" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="spol" required/>
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="email" required/>
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="lozinka" required/>
                    </div>
                    <div className="form-group">
                        <select required defaultValue="">
                            <option value="" disabled>Odaberi željeni smjer</option>
                            <option value="opca">Opća gimnazija</option>
                            <option value="prirodoslovno-matematicka">Prirodoslovno-matematička gimnazija</option>
                            <option value="jezicna">Jezična gimnazija</option>
                        </select>
                    </div>
                    <button type="submit">PRIJAVA</button>
                </form>
            </div>
        </div>
    );
}

export default Enroll;

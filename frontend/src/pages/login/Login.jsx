import React, { useState } from "react";
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const loginData = {
            email: email,
            lozinka: password
        };

        try {
            const response = await fetch("http://localhost:8080/loginUser", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(loginData),
            });

            if (response.ok) {
                const data = await response.text(); 
                setMessage(data);
            } else {
                const errorData = await response.text(); 
                setMessage(errorData || "Neuspjela prijava. Pokušajte ponovno.");
            }
        } catch (error) {
            setMessage("Došlo je do greške. Pokušajte ponovno.");
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input 
                        type="email" 
                        placeholder="Email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <input 
                        type="password" 
                        placeholder="Lozinka" 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                        required 
                    />
                </div>
                <button type="submit">Prijavi se</button>
            </form>
            <p>{message}</p>
        </div>
    );
};

export default Login;

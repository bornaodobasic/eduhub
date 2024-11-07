import React from "react";
import { useEffect, useState } from "react";
import './Login.css';

const Login = () => {
    const [userRole, setUserRole] = useState('');

    useEffect(() => {
        const role = localStorage.getItem('userRole'); 
        setUserRole(role || '');
    }, []);

    return (
        <div className="login-container">
            <form className="login-form">
                <div className="form-group">
                    <input type="text" placeholder="email" required/>
                </div>
                <div className="form-group">
                    <input type="text" placeholder="lozinka" required/>
                </div>
                <button type="submit">PRIJAVA</button>
            </form>
        </div>

    );
}

export default Login;
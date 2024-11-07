import React from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LogReg.css";

const LogReg = () => {
    const [userRole, setUserRole] = useState('');

    useEffect(() => {
        const role = localStorage.getItem('userRole'); 
        setUserRole(role || '');
    }, []);

    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/login"); 
    };

    const handleRegistration = () => {
        navigate("/registration"); 
    };

    const handleEnroll = () => {
        navigate("/enroll"); 
    };

    return (
        <div className="logreg-container">
            <button onClick={handleLogin}>
                PRIJAVA
            </button>
            <button onClick={handleRegistration}>
                REGISTRACIJA
            </button>
            {userRole === 'ucenik' && 
            
                <button onClick={handleEnroll}>
                    UPIS U 1. RAZRED
                </button>
            }
        </div>
    );
};

export default LogReg;

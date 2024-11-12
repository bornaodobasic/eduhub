import React from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LogReg.css";
import {useLocation} from "react-router-dom"

const LogReg = () => {
    const [userRole, setUserRole] = useState('');
    const location=useLocation()
    const queryParams=new URLSearchParams(location.search)
    useEffect(() => {
        
        let role=queryParams.get("role")
        //const role = localStorage.getItem('userRole'); 
        setUserRole(role || '');
    }, []);

    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/login?role="+userRole); 
    };

    const handleRegistration = () => {
        navigate("/registration?role="+userRole); 
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

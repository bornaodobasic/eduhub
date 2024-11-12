// src/components/BackButton.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './BackButton.css'; // Optional: create a CSS file for styling if needed

const BackButton = () => {
    const navigate = useNavigate();

    return (
        <button className="back-button" onClick={() => navigate('/admin-home')}>
            Natrag
        </button>
    );
};

export default BackButton;

import React from 'react';
import './UserInfo.css';

const UserInfo = () => {
    return (
        <div className="userInfo">
            <p><strong>Ime i Prezime:</strong> Vinko Jokić</p>
            <p><strong>Smjer:</strong> Matematika</p>
            <p><strong>Razred:</strong> 4.c </p>
            <button>Odjava</button>
        </div>
    );
};

export default UserInfo;

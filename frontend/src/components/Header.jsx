import React, { useState } from 'react';
import './Header.css';
import UserInfo from './UserInfo';

const Header = () => {
    const [showDropdown, setShowDropdown] = useState(false);

    const handleIconClick = () => {
        setShowDropdown((prev) => !prev); 
    };

    return (
        <header className="header">
            <div className="header-logo"></div>
            <div className="header-nazivSkole">Gimnazija Zagreb</div>
            <div className="header-ikonica" onClick={handleIconClick}>
            </div>
            
            
            {showDropdown && <UserInfo />}
        </header>
    );
};

export default Header;

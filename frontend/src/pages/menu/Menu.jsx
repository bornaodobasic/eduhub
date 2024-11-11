import React from "react";
import { useNavigate } from 'react-router-dom';
import "./Menu.css";

const Menu = () => {
    const navigate = useNavigate();

    const handleRoleSelect = (role) => {
        localStorage.setItem('userRole', role); 
        navigate('/login-registration'); 
    };

  return (
    <div className="container">
    <div className="row">
      <span className="name">eSkolskaKomunikacija</span>
        
    </div>
        <div className="row">
            <div className="square-button" id="ucenik" onClick={() => handleRoleSelect('ucenik')}>UČENIK</div>
            <div className="square-button" id="nastavnik" onClick={() => handleRoleSelect('nastavnik')}>NASTAVNIK</div>
            <div className="square-button" id="ravnatelj" onClick={() => handleRoleSelect('ravnatelj')}>RAVNATELJ</div>
        </div>
      
        <div className="row">
            <div className="square-button" id="satnicar" onClick={() => handleRoleSelect('satnicar')}>SATNIČAR</div>
            <div className="square-button" id="ucenicka-sluzba" onClick={() => handleRoleSelect('ucenicka-sluzba')}>UČENIČKA SLUŽBA</div>
            <div className="square-button"id="administrator" onClick={() => handleRoleSelect('administrator')}>ADMINISTRATOR</div>
        </div>
    </div>

  );
};

export default Menu;

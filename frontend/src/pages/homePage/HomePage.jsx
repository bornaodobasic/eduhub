import React from "react";
import "./HomePage.css";

const HomePage = () => {
  return (
    <div className="container">
    <div className="row">
        <span className="name">SREDNJA ŠKOLA</span>
    </div>
        <div className="row">
            <div className="square-button" id="ucenik">UČENIK</div>
            <div className="square-button" id="nastavnik">NASTAVNIK</div>
            <div className="square-button" id="ravnatelj">RAVNATELJ</div>
        </div>
      
        <div className="row">
            <div className="square-button" id="satnicar">SATNIČAR</div>
            <div className="square-button" id="ucenicka-sluzba">UČENIČKA SLUŽBA</div>
            <div className="square-button"id="administrator">ADMINISTRATOR</div>
        </div>
    </div>
  );
};

export default HomePage;

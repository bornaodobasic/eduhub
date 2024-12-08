import React from 'react';
import './Enroll.css';
import Header from '../../components/Header';
import Circles from '../../components/Circles';

const Enroll = () => {
  return (
    <div className="enroll-page">
      <Header></Header>
      <Circles></Circles>

      
      <main className="enroll-form">
        <h1>Upis u 1. razred</h1>
    
        <form>
          <div className="form-row">
           
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="firstName">Ime</label>
                <input type="text" id="firstName" placeholder="Unesite ime" />
              </div>

              <div className="form-group">
                <label htmlFor="oib">OIB</label>
                <input type="text" id="oib" placeholder="Unesite OIB" />
              </div>
             
              <div className="form-group">
                <label htmlFor="gender">Spol</label>
                <select id="gender">
                  <option value="">Odaberite spol</option>
                  <option value="muski">M</option>
                  <option value="zenski">Ž</option>
                </select>
              </div>
            </div>

            {/* Drugi Stupac */}
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="lastName">Prezime</label>
                <input type="text" id="lastName" placeholder="Unesite prezime" />
              </div>

              <div className="form-group">
                <label htmlFor="birthDate">Datum rođenja</label>
                <input type="date" id="birthDate" />
              </div>

              <div className="form-group">
                <label htmlFor="program">Željeni smjer</label>
                <select id="program">
                  <option value="">Odaberite željeni smjer</option>
                  <option value="opca">Opća gimnazija</option>
                  <option value="prirodoslovno-matematicka">Prirodoslovno-matematička gimnazija</option>
                  <option value="jezicna">Jezična gimnazija</option>
                </select>
              </div>
            </div>
          </div>

          <button type="submit" className="continue-button">Potvrdi</button>
        </form>
      </main>
    </div>
  );
};

export default Enroll;


import React, { useState } from 'react';
import './Enroll.css';
import Header from '../../components/Header';
import Circles from '../../components/Circles';

const Enroll = () => {
  const [formData, setFormData] = useState({
    imeUcenik: '',
    prezimeUcenik: '',
    oib: '',
    datumRodenja: '',
    spol: '',
    smjer: '',
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData({ ...formData, [id]: value });
  };

  const validateForm = () => {
    const { oib } = formData;
    if (!/^\d{11}$/.test(oib)) {
      alert('OIB must be 11 digits long');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const formDataObj = new FormData();
      Object.entries(formData).forEach(([key, value]) => {
        formDataObj.append(key, value);
      });

      const response = await fetch('/upis/ucenik', {
        method: 'POST',
        body: formDataObj,
      });

      if (response.ok) {
        setFormData({
          imeUcenik: '',
          prezimeUcenik: '',
          oib: '',
          datumRodenja: '',
          spol: '',
          smjer: '',
        });
        alert('Upis uspješan!');
        window.location.href = '/ucenik'; // Redirect to /ucenik page
      } else {
        const errorData = await response.text();
        alert(`Greška: ${errorData || 'Neuspješan upis'}`);
        console.error('Error:', errorData);
      }
    } catch (error) {
      console.error('Network error:', error);
      alert('Dogodila se greška prilikom povezivanja sa serverom.');
    }
  };

  return (
      <div className="enroll-page">
        <Header />
        <Circles />

        <main className="enroll-form">
          <h1>Upis u 1. razred</h1>

          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-column">
                <div className="form-group">
                  <label htmlFor="imeUcenik">Ime</label>
                  <input
                      type="text"
                      id="imeUcenik"
                      placeholder="Unesite ime"
                      value={formData.imeUcenik}
                      onChange={handleChange}
                      required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="oib">OIB</label>
                  <input
                      type="text"
                      id="oib"
                      placeholder="Unesite OIB"
                      value={formData.oib}
                      onChange={handleChange}
                      required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="spol">Spol</label>
                  <select
                      id="spol"
                      value={formData.spol}
                      onChange={handleChange}
                      required
                  >
                    <option value="">Odaberite spol</option>
                    <option value="muski">M</option>
                    <option value="zenski">Ž</option>
                  </select>
                </div>
              </div>

              <div className="form-column">
                <div className="form-group">
                  <label htmlFor="prezimeUcenik">Prezime</label>
                  <input
                      type="text"
                      id="prezimeUcenik"
                      placeholder="Unesite prezime"
                      value={formData.prezimeUcenik}
                      onChange={handleChange}
                      required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="datumRodenja">Datum rođenja</label>
                  <input
                      type="date"
                      id="datumRodenja"
                      value={formData.datumRodenja}
                      onChange={handleChange}
                      required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="smjer">Željeni smjer</label>
                  <select
                      id="smjer"
                      value={formData.smjer}
                      onChange={handleChange}
                      required
                  >
                    <option value="">Odaberite željeni smjer</option>
                    <option value="opca">Opća gimnazija</option>
                    <option value="prirodoslovno-matematicka">
                      Prirodoslovno-matematička gimnazija
                    </option>
                    <option value="jezicna">Jezična gimnazija</option>
                  </select>
                </div>
              </div>
            </div>

            <button type="submit" className="enroll-button">
              Potvrdi
            </button>
          </form>
        </main>
      </div>
  );
};

export default Enroll;
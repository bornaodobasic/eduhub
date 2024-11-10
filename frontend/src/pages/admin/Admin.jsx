import React, { useState, useEffect } from 'react';
import './Admin.css'; 

const Admin = () => {
  const [registrations, setRegistrations] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedRegistration, setSelectedRegistration] = useState(null);

  // za primjer, a kasnije iz backenda
  useEffect(() => {
    setRegistrations([
      { id: 1, ime: 'Ime Prezime', email: 'mail@fer.hr', uloga: 'Uloga', status: 'Na čekanju', datumReg: '10.11.2024' },
      { id: 2, ime: 'Ime Prezime', email: 'mail@fer.hr', uloga: 'Uloga', status: 'Na čekanju', datumReg: '9.11.2024' },
    ]);
  }, []);

  const approveRegistration = (id) => {
    setRegistrations((prev) =>
      prev.map((registration) =>
        registration.id === id ? { ...registration, status: 'Odobreno' } : registration
      )
    );
    setModalVisible(false);
  };

  const rejectRegistration = (id) => {
    setRegistrations((prev) =>
      prev.map((registration) =>
        registration.id === id ? { ...registration, status: 'Odbijeno' } : registration
      )
    );
    setModalVisible(false);
  };

  const handleModalOpen = (registration) => {
    setSelectedRegistration(registration);
    setModalVisible(true);
  };

  const handleModalClose = () => {
    setModalVisible(false);
    setSelectedRegistration(null);
  };

  return (
   
    <div className="admin-registration-page">
      <div className="admin-header"></div>

      <h1>Upravljanje registracijama</h1>

      <div className="search-bar">
        <input type="text" />
        <button>Pretraži</button>
      </div>

      <table className="registration-table">
        <thead>
          <tr>
            <th>Ime i prezime</th>
            <th>Email</th>
            <th>Uloga</th>
            <th>Status</th>
            <th>Akcija</th>
            <th>Datum registracije</th>
          </tr>
        </thead>
        <tbody>
          {registrations.map((registration) => (
            <tr key={registration.id}>
              <td>{registration.ime}</td>
              <td>{registration.email}</td>
              <td>{registration.uloga}</td>
              <td>{registration.status}</td>
              <td>
                <button className="approve-btn" onClick={() => handleModalOpen(registration)}>
                  Odobri
                </button>
                <button className="reject-btn" onClick={() => rejectRegistration(registration.id)}>
                  Odbij
                </button>
              </td>
              <td>{registration.datumReg}</td>
            </tr>
          ))}
        </tbody>
      </table>

      

      {modalVisible && selectedRegistration && (
        <div className="modal">
          <div className="modal-content">
            <h2>Potvrdi</h2>
            <p>
              Jeste li sigurni da želite odobriti registraciju{' '}
              <strong>{selectedRegistration.ime}</strong>?
            </p>
            <button className="confirm-btn" onClick={() => approveRegistration(selectedRegistration.id)}>
              Potvrdi
            </button>
            <button className="cancel-btn" onClick={handleModalClose}>
              Odbaci
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Admin;

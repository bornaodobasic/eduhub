import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/Header';
import SidebarRight from '../../components/SidebarRight';
import BackButton from '../../components/BackButton';
import '../approveadmin/ApproveAdmin.css';

const ApproveEmployee = () => {
    const [registrationRequests, setRegistrationRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/admin/zahtjevi/tempDjelatnik')
            .then(response => response.json())
            .then(data => {
                setRegistrationRequests(data);
                setLoading(false);
            })
            .catch(err => {
                setError('Failed to fetch');
                setLoading(false);
            });
    }, []);

    
    const handleApprove = (email) => {
        fetch(`http://localhost:8080/admin/zahtjevi/tempDjelatnik/${email}/odobri`, {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                setRegistrationRequests(prevRequests => 
                    prevRequests.filter(request => request.email !== email)
                );
            } else {
                alert('Failed to approve the request.');
            }
        })
        .catch(() => alert('Failed to approve the request.'));
    };

    
    const handleReject = (email) => {
        fetch(`http://localhost:8080/admin/zahtjevi/tempDjelatnik/${email}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (response.ok) {
                setRegistrationRequests(prevRequests => 
                    prevRequests.filter(request => request.email !== email)
                );
            } else {
                alert('Failed to reject the request.');
            }
        })
        .catch(() => alert('Failed to reject the request.'));
    };

    const roles = [
        { name: "Administrator", path: "/approveadmin" },
        { name: "Djelatnik", path: "/approveemployee" },
        { name: "Učenik", path: "/approveucenik" },
        { name: "Nastavnik", path: "/approvenastavnik" },
        { name: "Ravnatelj", path: "/approveravnatelj" },
        { name: "Satničar", path: "/approvesatnicar" }
    ];

    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">
                <aside className="sidebar-left">
                    <button className="sidebar-button gray">Zahtjevi za registraciju</button>
                    {roles.map(role => (
                        <Link key={role.name} to={role.path}>
                            <button className="sidebar-button">{role.name}</button>
                        </Link>
                    ))}
                </aside>
                <div className="main-content">
                    <h2>Zahtjevi za registraciju djelatnika</h2>
                    {loading && <p>Loading...</p>}
                    {error && <p>{error}</p>}
                    {!loading && !error && (
                        <table className="requests-table">
                            <thead>
                                <tr>
                                    <th>Ime</th>
                                    <th>Prezime</th>
                                    <th>Email</th>
                                    <th>Akcije</th>
                                </tr>
                            </thead>
                            <tbody>
                                {registrationRequests.map(request => (
                                    <tr key={request.email}>
                                        <td>{request.imeAdmin}</td>
                                        <td>{request.prezimeAdmin}</td>
                                        <td>{request.email}</td>
                                        <td>
                                            <button 
                                                className="approve-btn" 
                                                onClick={() => handleApprove(request.email)}
                                            >
                                                Approve
                                            </button>
                                            <button 
                                                className="reject-btn" 
                                                onClick={() => handleReject(request.email)}
                                            >
                                                Reject
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
                <SidebarRight />
            </div>
        </div>
    );
};

export default ApproveEmployee;
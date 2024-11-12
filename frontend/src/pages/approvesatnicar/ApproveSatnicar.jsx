import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/Header';
import SidebarRight from '../../components/SidebarRight';
import BackButton from '../../components/BackButton';
import '../approveadmin/ApproveAdmin.css';


const ApproveSatnicar = () => {
    const roles = [
        { name: "Administrator", path: "/approveadmin" },
        { name: "Djelatnik", path: "/approveemployee" },
        { name: "Učenik", path: "/approveucenik" },
        { name: "Nastavnik", path: "/approvenastavnik" },
        { name: "Ravnatelj", path: "/approveravnatelj" },
        { name: "Satničar", path: "/approvesatnicar" }
    ];

    const registrationRequests = [
        { id: 1, firstName: "John", lastName: "Doe", email: "john.doe@example.com"},
        { id: 2, firstName: "Jane", lastName: "Smith", email: "jane.smith@example.com" },
        { id: 3, firstName: "Robert", lastName: "Brown", email: "robert.brown@example.com" },
        // More requests can be added here
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
                    <h2>Zahtjevi za registraciju satničara</h2>
                    <table className="requests-table">
                        <thead>
                            <tr>
                                <th>Ime</th>
                                <th>Prezime</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            {registrationRequests.map(request => (
                                <tr key={request.id}>
                                    <td>{request.firstName}</td>
                                    <td>{request.lastName}</td>
                                    <td>{request.email}</td>
                                    <td>
                                        <button className="approve-btn">Approve</button>
                                        <button className="reject-btn">Reject</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
                <SidebarRight />
            </div>
        </div>
    );
};

export default ApproveSatnicar;

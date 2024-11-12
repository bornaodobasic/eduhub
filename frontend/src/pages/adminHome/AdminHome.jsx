import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/Header';
import SidebarRight from '../../components/SidebarRight';
import MainContent from '../../components/MainContent';
import './AdminHome.css';

const AdminHome = () => {
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
                <MainContent />
                <SidebarRight />
            </div>
        </div>
    );
};

export default AdminHome;

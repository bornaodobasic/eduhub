import React from 'react';
import Header from '../../components/Header';
import SidebarRight from '../../components/SidebarRight';
import MainContent from '../../components/MainContent';
import './AdminHome.css';

const Adminhome = () => {
    const roles = [
        "Administrator", "Djelatnik", "Učenik", "Nastavnik", "Ravnatelj", "Satničar"
    ];

    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">
                <aside className="sidebar-left">
                <button className="sidebar-button gray">Zahtjevi za registraciju</button>
                    {roles.map(role => (
                        <button key={role} className="sidebar-button">{role}</button>
                    ))}
                </aside>
                <MainContent />
                <SidebarRight />
            </div>
        </div>
    );
};

export default Adminhome;

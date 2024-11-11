import React from 'react';
import Header from '../../components/Header';
import SidebarLeft from '../../components/SidebarLeft';
import SidebarRight from '../../components/SidebarRight';
import MainContent from '../../components/MainContent';
import './Homepage.css';

const Homepage = () => {
    return (
        <div className="homepage">
            <Header />
            <div className="homepage-container">
                <SidebarLeft />
                <MainContent />
                <SidebarRight />
            </div>
        </div>
    );
};

export default Homepage;

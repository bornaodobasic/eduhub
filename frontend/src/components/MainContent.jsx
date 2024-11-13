import React, { useState } from 'react';
import './MainContent.css';

const MainContent = () => {
    const [activeTab, setActiveTab] = useState("KALENDAR");

    return (
        <main className="main-content">
            <div className="tabs">
                {["KALENDAR", "OBAVIJESTI", "CHAT"].map(tab => (
                    <button
                        key={tab}
                        className={`tab-button ${activeTab === tab ? 'active' : ''}`}
                        onClick={() => setActiveTab(tab)}
                    >
                        {tab}
                    </button>
                ))}
            </div>
            <div className="content-area">
                {`${activeTab}`}
            </div>
        </main>
    );
};

export default MainContent;

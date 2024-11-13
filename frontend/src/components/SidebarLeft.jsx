import React from 'react';
import './SidebarLeft.css';

const subjects = [
    "Matematika", "Fizika", "Kemija", "Biologija", "Informatika", "Psihologija", "Filozofija"
];

const SidebarLeft = () => (
    <aside className="sidebar-left">
        {subjects.map(subject => (
            <button key={subject} className="sidebar-button">{subject}</button>
        ))}
    </aside>
);

export default SidebarLeft;

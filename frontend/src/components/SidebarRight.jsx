import React from 'react';
import Weather from './Weather';
import './SidebarRight.css';

const SidebarRight = () => (
    <aside className="sidebar-right">
        <div className='weather'>
            <Weather />
        </div>
       
    </aside>
);

export default SidebarRight;

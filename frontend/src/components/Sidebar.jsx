import React, { useState } from "react";
import { FaSignOutAlt, FaBars } from "react-icons/fa";
import "./Sidebar.css";

const Sidebar = ({ activeSection, setActiveSection, menuItems }) => {
  const [isSidebarOpen, setSidebarOpen] = useState(false); // Kontrola prikaza sidebar-a

  const toggleSidebar = () => {
    setSidebarOpen(!isSidebarOpen); // Prebacivanje otvoreno/zatvoreno
  };

  const handleMenuItemClick = (name) => {
    setActiveSection(name);
    setSidebarOpen(false); // Zatvori sidebar nakon klika na sekciju
  };

  const handleLogout = () => {
    window.location.href = "/logout";
  };

  return (
    <>
      {/* Hamburger meni i logo za mobilni prikaz */}
      <div className="mobile-header">
        <FaBars className="hamburger-icon" onClick={toggleSidebar} />
      </div>

      {/* Sidebar */}
      <div className={`sidebar ${isSidebarOpen ? "open" : ""}`}>
        <div className="sidebar-logo">EduHub</div>
        <ul className="sidebar-menu">
          {menuItems.map((item, index) => (
            <li
              key={index}
              className={`sidebar-item ${activeSection === item.name ? "active" : ""}`}
              onClick={() => handleMenuItemClick(item.name)}
            >
              {item.icon}
              <span>{item.name}</span>
            </li>
          ))}
        </ul>
        <div className="sidebar-logout" onClick={handleLogout}>
          <FaSignOutAlt />
          <span>Odjava</span>
        </div>
      </div>

      {/* Overlay (zatvara sidebar klikom izvan njega) */}
      {isSidebarOpen && <div className="sidebar-overlay" onClick={toggleSidebar}></div>}
    </>
  );
};

export default Sidebar;

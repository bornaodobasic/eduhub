import React from "react";
import { FaSignOutAlt } from "react-icons/fa";
import "./Sidebar.css";

const Sidebar = ({ activeSection, setActiveSection, menuItems }) => {
 

  const handleLogout = async () => {
    window.location.href = "/logout";
  };

  return (
    <div className="sidebar">
      <div className="sidebar-logo">EduHub</div>
      <ul className="sidebar-menu">
        {menuItems.map((item, index) => (
          <li
            key={index}
            className={`sidebar-item ${activeSection === item.name ? "active" : ""}`}
            onClick={() => setActiveSection(item.name)}
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
  );
};

export default Sidebar;
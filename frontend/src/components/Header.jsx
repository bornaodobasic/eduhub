import React, { useState } from 'react';
import './Header.css';

const Header = () => {
  // Placeholder for user's name
  const userName = "User Name"; // Replace with dynamic data as needed

  // State to toggle the form visibility on hover
  const [showLogout, setShowLogout] = useState(false);

  return (
    <header className="header">
      <div className="logo">
        <span>eŠkolskaKomunikacija</span>
      </div>
      <div className="user-info">
        <span className="user-name">{userName}</span>
        <div
          className="profile-icon-container"
          onMouseEnter={() => setShowLogout(true)}
          onMouseLeave={() => setShowLogout(false)}
        >
         
          <svg
            height="30"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              clipRule="evenodd"
              d="M12 4C7.58172 4 4 7.58172 4 12C4 14.0991 4.80806 16.0099 6.13199 17.4377C6.18198 17.2724 6.24846 17.1099 6.33518 16.9549C6.62917 16.4292 7.00004 15.9432 7.44009 15.5134C7.88171 15.082 8.37995 14.7201 8.91734 14.4341C8.09887 13.6506 7.58716 12.5557 7.58716 11.3333C7.58716 8.91801 9.58521 7 12 7C14.4148 7 16.4128 8.91801 16.4128 11.3333C16.4128 12.5557 15.9011 13.6506 15.0827 14.4341C15.6201 14.7201 16.1183 15.082 16.5599 15.5134C17 15.9432 17.3708 16.4292 17.6648 16.9549C17.7515 17.1099 17.818 17.2724 17.868 17.4377C19.1919 16.0099 20 14.0991 20 12C20 7.58172 16.4183 4 12 4ZM15.9411 18.9637C15.9799 18.7639 15.9994 18.5764 16 18.4103C16.0009 18.1358 15.9507 17.9872 15.9193 17.9312C15.7187 17.5726 15.465 17.2397 15.1625 16.9442C14.3277 16.1288 13.1905 15.6667 12 15.6667C10.8095 15.6667 9.67234 16.1288 8.83754 16.9442C8.535 17.2396 8.28126 17.5726 8.0807 17.9312C8.04934 17.9872 7.99905 18.1358 8.00001 18.4103C8.0006 18.5764 8.02005 18.7639 8.05888 18.9637C9.22151 19.6233 10.5655 20 12 20C13.4345 20 14.7785 19.6233 15.9411 18.9637ZM12 13.6667C13.3549 13.6667 14.4128 12.5999 14.4128 11.3333C14.4128 10.0668 13.3549 9 12 9C10.6451 9 9.58716 10.0668 9.58716 11.3333C9.58716 12.5999 10.6451 13.6667 12 13.6667ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 15.5573 20.1419 18.6798 17.348 20.4512C15.8007 21.4321 13.9651 22 12 22C10.0349 22 8.19929 21.4321 6.65202 20.4512C3.85814 18.6798 2 15.5573 2 12Z"
              fill="white"
            />
          </svg>

          {showLogout && (
            <div className="logout-form">
              <button className="logout-btn">Log Out</button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;



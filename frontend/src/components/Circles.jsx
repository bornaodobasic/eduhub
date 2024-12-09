import React from 'react';
import './Circles.css';


const Circles = () => {
    return (

<div className="svg-background">
<svg
  viewBox="0 0 1600 800"
  xmlns="http://www.w3.org/2000/svg"
  className="decorative-svg"
  width="100vw"
  height="50vh"
  preserveAspectRatio="xMidYMid slice"
>
  <defs>
    <linearGradient id="gradient1" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stopColor="#FFB6C1" /> 
      <stop offset="100%" stopColor="#87CEFA" /> 
    </linearGradient>
    <linearGradient id="gradient2" x1="0%" y1="0%" x2="100%" y2="0%">
      <stop offset="0%" stopColor="#FFD700" /> 
      <stop offset="100%" stopColor="#32CD32" />
    </linearGradient>
    
  </defs>

  <path class="responsive-path" 
    d="M 0 0 L 1600 0 L 1600 250 Q 1200 450 800 250 Q 400 50 0 250 Z"
    fill="url(#gradient1)"
    opacity="0.5"
  />

  <circle className="responsive-circle" cx="1500" cy="350" r="180"fill="#00FA9A" opacity="0.3"/>
  <circle className="responsive-circle" cx="1340" cy="600" r="30" fill="#ff758c" opacity="0.3" /> 
  <circle className="responsive-circle" cx="150" cy="800" r="240"fill="#FFFF00" opacity="0.3"/>
</svg>
</div>

);
};

export default Circles;
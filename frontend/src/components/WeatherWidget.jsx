import React, { useState, useEffect } from 'react';
import './WeatherWidget.css';

const WeatherWidget = () => {
  const [weather, setWeather] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchWeather = async () => {
      try {
        const response = await fetch(
          'http://api.weatherapi.com/v1/current.json?key=75865835572b41f7a15192310241712&q=Zagreb'
        );
        if (!response.ok) {
          throw new Error('Podaci o vremenu ne mogu se dohvatiti');
        }
        const data = await response.json();
        setWeather({
          city: data.location.name,
          icon: data.current.condition.icon,
          temp: data.current.temp_c,
        });
      } catch (err) {
        setError(err.message);
      }
    };

    fetchWeather();
  }, []);

  if (error) return <div className="weather-error">Error: {error}</div>;
  if (!weather) return <div className="weather-loading">Loading...</div>;

  return (
    <div className="weather-widget">
      <div className="weather-info">
        <h4>{weather.city}</h4>
        <p>{weather.temp}°C</p>
      </div>
      <img
        src={`https:${weather.icon}`}
        alt="Weather Icon"
        className="weather-icon"
      />
    </div>
  );
};

export default WeatherWidget;
import React, { useState, useEffect } from 'react';
import './WeatherWidget.css';

const WeatherWidget = () => {
  const [weather, setWeather] = useState({
    temperature: null,
    city: "Zagreb",
    icon: null,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchWeather = async () => {
      try {

        const openWeatherApiKey = "a4f98e822447d3d5867c3d29dd0cef26";
        const openWeatherResponse = await fetch(
            `https://api.openweathermap.org/data/2.5/weather?q=Zagreb,hr&units=metric&appid=${openWeatherApiKey}`
        );
        const openWeatherData = await openWeatherResponse.json();

        if (openWeatherResponse.ok) {
          const icon = `http://openweathermap.org/img/wn/${openWeatherData.weather[0].icon}@2x.png`;


          const weatherApiKey = "75865835572b41f7a15192310241712";
          const weatherApiResponse = await fetch(
              `http://api.weatherapi.com/v1/current.json?key=${weatherApiKey}&q=Zagreb`
          );
          const weatherApiData = await weatherApiResponse.json();

          if (weatherApiResponse.ok) {
            const temperature = weatherApiData.current.temp_c;

            setWeather({
              temperature: Math.round(temperature),
              city: "Zagreb",
              icon: icon,
            });
          } else {
            throw new Error('Podaci o vremenu ne mogu se dohvatiti');
          }
        } else {
          throw new Error('Ikona se ne može dohvatiti');
        }
      } catch (error) {
        console.error("Greška pri dohvatu podataka:", error);
        setError(error.message);
        setWeather(prevState => ({
          ...prevState,
          temperature: null,
          icon: null,
        }));
      } finally {
        setLoading(false);
      }
    };

    fetchWeather();
  }, []);

  if (error) return <div className="weather-error">Error: {error}</div>;
  if (loading) return <div className="weather-loading">Loading...</div>;

  return (
      <div className="weather-widget">
        <div className="weather-info">
          <img
              src={weather.icon}
              alt="Weather Icon"
              className="weather-icon"
          />
          <p>{weather.temperature}°C</p>
          <h4>{weather.city}</h4>
        </div>
      </div>
  );
};

export default WeatherWidget;
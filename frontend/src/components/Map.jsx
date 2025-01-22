import React, { useState } from "react";
import { MapContainer, TileLayer, Polyline, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import axios from "axios";

const Map = () => {
  const [route, setRoute] = useState(null);
  const [street, setStreet] = useState("");
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("");
  const fixedStart = [45.815, 15.9819]; // Zagreb

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const API_KEY = "pk.9f3c0bc1a4e1536bac2668e742285686";
      const query = `${street}, ${city}, ${country}`;
      const response = await axios.get(
        `https://us1.locationiq.com/v1/search.php?key=${API_KEY}&q=${encodeURIComponent(query)}&format=json`
      );

      if (response.data.length > 0) {
        const { lat, lon } = response.data[0];
        const endCoordinates = [parseFloat(lat), parseFloat(lon)];

        setRoute([fixedStart, endCoordinates]);
      } else {
        alert("Odredište nije pronađeno. Pokušaj ponovno.");
      }
    } catch (error) {
      console.error("Greška pri dohvaćanju podataka:", error);
    }
  };

  return (
    <div>
      <h1>Prikaz rute</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Unesite ulicu"
          value={street}
          onChange={(e) => setStreet(e.target.value)}
        />
        <input
          type="text"
          placeholder="Unesite grad"
          value={city}
          onChange={(e) => setCity(e.target.value)}
        />
        <input
          type="text"
          placeholder="Unesite državu"
          value={country}
          onChange={(e) => setCountry(e.target.value)}
        />
        <button type="submit">Prikaži rutu</button>
      </form>

      {/* Mapa */}
      <MapContainer center={fixedStart} zoom={6} style={{ height: "500px", width: "100%" }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {route && (
          <>
            <Marker position={fixedStart}>
              <Popup>Polazište: Zagreb</Popup>
            </Marker>
            <Marker position={route[1]}>
              <Popup>Odredište: {`${street}, ${city}, ${country}`}</Popup>
            </Marker>
            <Polyline positions={route} />
          </>
        )}
      </MapContainer>
    </div>
  );
};

export default Map;

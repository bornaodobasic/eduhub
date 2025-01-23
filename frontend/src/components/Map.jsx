import React, { useState } from "react";
import { MapContainer, TileLayer, Polyline, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import axios from "axios";

const Map = ({ street, city, country }) => {
  const [route, setRoute] = useState(null);
  const fixedStart = [45.815, 15.9819]; // Zagreb - fiksno polazište

  const fetchRoute = async () => {
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

  // Pokreće se kada se komponenta prvi put učita ili kad se promijene `street`, `city`, `country`
  React.useEffect(() => {
    if (street && city && country) {
      fetchRoute();
    }
  }, [street, city, country]);

  return (
    <div>
      {/* Prikaz mape */}
      <MapContainer center={fixedStart} zoom={6} style={{ height: "500px", width: "100%" }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {route && (
          <>
            {/* Marker za polazište */}
            <Marker position={fixedStart}>
              <Popup>Polazište: Zagreb</Popup>
            </Marker>
            {/* Marker za odredište */}
            <Marker position={route[1]}>
              <Popup>Odredište: {`${street}, ${city}, ${country}`}</Popup>
            </Marker>
            {/* Prikaz rute */}
            <Polyline positions={route} />
          </>
        )}
      </MapContainer>
    </div>
  );
};

export default Map;

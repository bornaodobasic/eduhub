import React, { useEffect, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import "leaflet-routing-machine";
import "leaflet-control-geocoder"; // Ovo je ključno za geokodiranje

const MapRoute = () => {
  const [destination, setDestination] = useState("");
  const [map, setMap] = useState(null);
  const [routingControl, setRoutingControl] = useState(null);

  useEffect(() => {
    const initMap = L.map("map").setView([45.815, 15.9819], 13);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "© OpenStreetMap contributors",
    }).addTo(initMap);

    setMap(initMap);

    const control = L.Routing.control({
      waypoints: [L.latLng(45.767735, 15.960889)],
      routeWhileDragging: true,
      lineOptions: {
        styles: [{ color: "blue", weight: 4 }],
      },
      createMarker: (i, waypoint, n) => {
        return L.marker(waypoint.latLng, { draggable: true });
      },
    }).addTo(initMap);

    setRoutingControl(control);

    return () => {
      initMap.remove();
    };
  }, []);

  const handleGeocode = () => {
    if (!destination) {
      alert("Unesite grad i državu za odredište.");
      return;
    }

    // Koristimo Leaflet.Control.Geocoder za geokodiranje
    const geocoder = L.Control.Geocoder.nominatim(); // Inicijaliziramo geocoder
    geocoder.geocode(destination, (results) => {
      if (results && results.length > 0) {
        const destLatLng = results[0].center; // Koordinate pronađene lokacije
        if (routingControl) {
          routingControl.setWaypoints([
            L.latLng(45.767735, 15.960889), // Polazište
            destLatLng, // Novo odredište
          ]);
          map.setView(destLatLng, 13); // Fokusiramo mapu na novu lokaciju
        }
      } else {
        alert("Odredište nije pronađeno. Pokušajte ponovno.");
      }
    });
  };

  return (
    <div>
      <div style={{ marginBottom: "10px" }}>
        <input
          type="text"
          placeholder="Unesite grad i državu (npr. Rijeka, Hrvatska)"
          value={destination}
          onChange={(e) => setDestination(e.target.value)}
          style={{ width: "300px", marginRight: "10px", padding: "5px" }}
        />
        <button onClick={handleGeocode} style={{ padding: "5px 10px" }}>
          Postavi odredište
        </button>
      </div>
      <div id="map" style={{ height: "500px", width: "100%" }}></div>
    </div>
  );
};

export default MapRoute;

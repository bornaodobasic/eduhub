import React, { useEffect } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import "leaflet-routing-machine";

const MapRoute = () => {
  useEffect(() => {
    const map = L.map("map").setView([45.815, 15.9819], 13);
  
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: '© OpenStreetMap contributors',
    }).addTo(map);

    L.Routing.control({
      waypoints: [
        L.latLng(45.767735, 15.960889), // Perjasička ulica 17a, Zagreb
        L.latLng(45.7685, 15.9691), // Unska ulica 3, Zagreb
      ],
      routeWhileDragging: true,
      lineOptions: {
        styles: [{ color: "blue", weight: 4 }],
      },
      createMarker: function (i, waypoint, n) {
        return L.marker(waypoint.latLng, {
          draggable: true,
        });
      },
    }).addTo(map);

    return () => {
      map.remove();
    };
  }, []);

  return <div id="map" style={{ height: "500px", width: "100%" }}></div>;
};

export default MapRoute;

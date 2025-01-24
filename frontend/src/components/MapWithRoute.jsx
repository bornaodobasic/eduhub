import React, { useState } from "react";
import { MapContainer, TileLayer, Marker, Polyline } from "react-leaflet";
import "leaflet/dist/leaflet.css";

const initialCoordinates = [45.767735, 15.960889]; // Zagreb
const splitCoordinates = [43.508132, 16.440193]; // Split

const MapWithRoute = () => {
  const route = [initialCoordinates, splitCoordinates];

  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h1>Route from Zagreb to Split</h1>

      <MapContainer
        center={initialCoordinates}
        zoom={6}
        style={{ height: "500px", width: "100%" }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="&copy; <a href='https://www.openstreetmap.org/copyright'>OpenStreetMap</a> contributors"
        />
        <Marker position={initialCoordinates}></Marker>
        <Marker position={splitCoordinates}></Marker>
        <Polyline positions={route} color="blue" />
      </MapContainer>
    </div>
  );
};

export default MapWithRoute;
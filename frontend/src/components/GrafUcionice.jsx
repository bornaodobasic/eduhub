import React, { useEffect, useState } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";


const GrafUcionice = () => {
  const [data, setData] = useState([]);

  const fetchUcionice = async () => {
    try {
      const response = await fetch("/api/ravnatelj/ucionice");
      if (!response.ok) throw new Error("Greška pri dohvaćanju učionica.");
      const result = await response.json();

      const formattedData = result.map((ucionica) => ({
        oznaka: ucionica.oznakaUc,
        kapacitet: ucionica.kapacitet,
      }));

      setData(formattedData);
    } catch (err) {
      console.error("Greška:", err.message);
    }
  };

  useEffect(() => {
    fetchUcionice();
  }, []);

  return (
    <div className="graf-container">
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="oznaka" label={{ value: "Učionice", position: "insideBottom", offset: -5 }} />
          <YAxis
            domain={[20, "auto"]} // Počinje od 20
            label={{ value: "Kapacitet", angle: -90, position: "insideLeft" }}
          />
          <Tooltip />
          <Bar dataKey="kapacitet" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default GrafUcionice;
import React, { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';
import './Zauzece.css';

const Zauzece = () => {
  const [data, setData] = useState([]);
  const COLORS = ['#0088FE', '#FF6384', '#FFBB28', '#36A2EB', '#4BC0C0', '#FFCE56', '#A28DFF', '#E8743B']; // Različite boje

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/satnicar/zauzece'); // Promijeni s punim URL-om ako je potrebno
        if (!response.ok) throw new Error(`HTTP greška! Status: ${response.status}`);
        const jsonData = await response.json();

        // Priprema podataka: svaki unos ima zauzeće i slobodno
        const chartData = Object.keys(jsonData).map(key => ({
          name: key,
          value: parseFloat((jsonData[key] * 100).toFixed(2)), // Pretvori u postotke
        }));

        setData(chartData);
      } catch (error) {
        console.error('Greška prilikom dohvaćanja podataka:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="charts-container">
      {data.map((entry, index) => (
        <div className="chart-item" key={index}>
          <ResponsiveContainer width={150} height={150}>
            <PieChart>
              <Pie
                data={[
                  { name: 'Zauzeto', value: entry.value },
                  { name: 'Slobodno', value: 100 - entry.value },
                ]}
                dataKey="value"
                cx="50%"
                cy="50%"
                innerRadius={50} // Rupa unutar kruga
                outerRadius={70} // Vanjski radijus
                startAngle={90}
                endAngle={-270}
              >
                {/* Različite boje za zauzeti dio */}
                <Cell key="zauzeto" fill={COLORS[index % COLORS.length]} />
                <Cell key="slobodno" fill="#E8E8E8" /> {/* Neutralna boja za slobodno */}
              </Pie>
            </PieChart>
          </ResponsiveContainer>
          <p className="chart-label">{entry.name}: {entry.value.toFixed(2)}%</p>
        </div>
      ))}
    </div>
  );
};

export default Zauzece;

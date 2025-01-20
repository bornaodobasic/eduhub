import React, { useState, useEffect } from "react";
import { FaEye, FaSort, FaSortUp, FaSortDown } from "react-icons/fa";

const TableUceniciNastavnik = ({ selectedSubject, fetchReport,
    setSelectedStudent,
    setSelectedStudentSubject, }) => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortConfig, setSortConfig] = useState({
    key: "prezimeUcenik",
    direction: "asc",
  });

  // Dohvaća učenike za odabrani predmet
  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const response = await fetch(
          `/api/nastavnik/uceniciNaPredmetu?nazPredmet=${selectedSubject}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
          }
        );
        if (!response.ok) throw new Error("Greška pri dohvaćanju učenika.");
        const result = await response.json();
        setStudents(result);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    if (selectedSubject) {
      fetchStudents();
    }
  }, [selectedSubject]);

  // Sortiranje podataka
  const sortedData = [...students].sort((a, b) => {
    if (a[sortConfig.key] < b[sortConfig.key]) {
      return sortConfig.direction === "asc" ? -1 : 1;
    }
    if (a[sortConfig.key] > b[sortConfig.key]) {
      return sortConfig.direction === "asc" ? 1 : -1;
    }
    return 0;
  });

  const requestSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });
  };

  const getSortIcon = (key) => {
    if (sortConfig.key === key) {
      return sortConfig.direction === "asc" ? <FaSortUp /> : <FaSortDown />;
    }
    return <FaSort />;
  };

  if (loading) return <p>Učitavanje učenika...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="table-container">
      <h2 className="table-title">
        Popis učenika za predmet: {selectedSubject.replace(/_/g, " ")}
      </h2>
      <table className="table">
        <thead>
          <tr>
            <th onClick={() => requestSort("prezimeUcenik")}>
              {getSortIcon("prezimeUcenik")} Prezime i ime
            </th>
            <th>Email</th>
            <th>Akcija</th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((student) => (
            <tr key={student.id}>
              <td>
                {student.prezimeUcenik} {student.imeUcenik}
              </td>
              <td>{student.email}</td>
              <td className="action-icons">
                {/* Klik na ikonu oka poziva `fetchReport` */}
                <FaEye
  className="icon view-icon"
  title="Pregled preuzetih materijala"
  onClick={() => {
    setSelectedStudent(student.email); // Postavi email učenika
    setSelectedStudentSubject(selectedSubject); // Postavi predmet
    fetchReport(); // Dohvati izvještaj
  }}
/>


              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TableUceniciNastavnik;

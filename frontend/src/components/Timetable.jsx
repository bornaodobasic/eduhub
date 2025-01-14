import React, { useState, useEffect } from "react";
/*import "./Timetable.css";*/

const Timetable = ({ email }) => {
    const [timetable, setTimetable] = useState([]);
    const [error, setError] = useState(null);

    // Fetch timetable data from the backend
    useEffect(() => {
        const fetchTimetable = async () => {
            try {
                const response = await fetch(`/api/ucenik/uceniknew%40eduxhub.onmicrosoft.com/raspored`);
                if (response.ok) {
                    const data = await response.json();
                    setTimetable(data);
                } else {
                    setError("Failed to fetch timetable data.");
                }
            } catch (err) {
                setError("An error occurred while fetching timetable data.");
                console.error(err);
            }
        };

        fetchTimetable();
    }, [email]);

    // Convert day enum to readable string
    const dayMapping = {
        MONDAY: "Monday",
        TUESDAY: "Tuesday",
        WEDNESDAY: "Wednesday",
        THURSDAY: "Thursday",
        FRIDAY: "Friday",
    };

    return (
        <div className="timetable">
            <div className="week-names">
                {Object.values(dayMapping).map((day) => (
                    <div key={day}>{day}</div>
                ))}
            </div>

            <div className="time-interval">
                {Array.from({ length: 8 }, (_, i) => `${8 + i}:00`).map((time) => (
                    <div key={time}>{time}</div>
                ))}
            </div>

            <div className="content">
                {error ? (
                    <div className="error-message">{error}</div>
                ) : (
                    timetable.map((entry, index) => (
                        <div
                            key={index}
                            className={`timetable-entry accent-${index % 6}-gradient`}
                            style={{
                                gridColumn: dayMapping[entry.dan],
                                gridRow: `${entry.pocetakSata.hour - 7}/${entry.krajSata.hour - 7}`,
                            }}
                        >
                            <strong>{entry.predmet}</strong>
                            <br />
                            {entry.nastavnik}
                            <br />
                            {entry.ucionica}
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Timetable;

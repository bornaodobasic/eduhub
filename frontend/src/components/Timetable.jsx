import React, { useState, useEffect } from "react";
import "./Timetable.css";

const Timetable = ({ email }) => {
    const [timetable, setTimetable] = useState([]);
    const [error, setError] = useState(null);

    // Fetch timetable data from the backend
    useEffect(() => {
        const fetchTimetable = async () => {
            if (!email) return; // Wait until email is available
            try {
                const response = await fetch(`/api/ucenik/${email}/raspored`);
                if (response.ok) {
                    const data = await response.json();
                    setTimetable(data);
                } else {
                    setError("Nije moguće dohvatiti raspored.");
                }
            } catch (err) {
                setError("Došlo je do pogreške pri dohvaćanju rasporeda.");
                console.error(err);
            }
        };

        fetchTimetable();
    }, [email]);

    // Map day enums to Croatian names
    const dayMapping = {
        MONDAY: "Ponedjeljak",
        TUESDAY: "Utorak",
        WEDNESDAY: "Srijeda",
        THURSDAY: "Četvrtak",
        FRIDAY: "Petak",
    };

    // Group timetable entries by `dan`
    const groupedTimetable = timetable.reduce((acc, entry) => {
        if (!acc[entry.dan]) {
            acc[entry.dan] = [];
        }
        acc[entry.dan].push(entry);
        return acc;
    }, {});

    // Helper function to calculate grid row based on start time
    const getGridRow = (startTime) => {
        const timeSlots = {
            "08:00:00": 1,
            "08:50:00": 2,
            "09:40:00": 3,
            "10:30:00": 4,
            "11:30:00": 5,
            "12:20:00": 6,
            "13:10:00": 7
        };
        return timeSlots[startTime] || 0; // Default to 0 if no match is found
    };

    return (
        <div className="timetable">
            {/* Weekdays */}
            <div className="week-names">
                {Object.values(dayMapping).map((day) => (
                    <div key={day}>{day}</div>
                ))}
            </div>

            {/* Time Intervals */}
            <div className="time-interval">
                {["08:00", "08:50", "09:40", "10:30", "11:30", "12:20", "13:10"].map((time) => (
                    <div key={time}>{time}</div>
                ))}
            </div>

            {/* Timetable Entries */}
            <div className="content">
                {error ? (
                    <div className="error-message">{error}</div>
                ) : (
                    // Iterate through each day
                    Object.keys(dayMapping).map((dan, columnIndex) => (
                        <div key={dan} className="day-column">
                            {/* Render all classes for the current day */}
                            {groupedTimetable[dan]?.map((entry, rowIndex) => (
                                <div
                                    key={entry.predmet + rowIndex}
                                    className={`timetable-entry accent-${rowIndex % 6}-gradient`}
                                    style={{
                                        gridColumn: columnIndex + 1, // Place the entry in the correct column
                                        gridRow: getGridRow(entry.pocetakSata), // Place the entry in the correct row based on start time
                                    }}
                                >
                                    <strong>{entry.predmet}</strong>
                                    <br />
                                    {entry.nastavnik}
                                    <br />
                                    {entry.ucionica}
                                    <br />
                                    {entry.pocetakSata}
                                </div>
                            ))}
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Timetable;

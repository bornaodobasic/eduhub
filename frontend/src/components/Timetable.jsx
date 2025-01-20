import React, { useState, useEffect } from "react";
import "./Timetable.css";

const Timetable = ({ email }) => {
    const [timetable, setTimetable] = useState([]);
    const [error, setError] = useState(null);

    const timeIntervals = [
        "08:00 - 08:45",
        "08:50 - 09:35",
        "09:40 - 10:25",
        "10:30 - 11:15",
        "11:30 - 12:15",
        "12:20 - 13:05",
        "13:10 - 13:55",
    ];

    useEffect(() => {
        const fetchTimetable = async () => {
            if (!email) return;
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

    const dayMapping = {
        MONDAY: "Ponedjeljak",
        TUESDAY: "Utorak",
        WEDNESDAY: "Srijeda",
        THURSDAY: "Četvrtak",
        FRIDAY: "Petak",
    };

    const groupedTimetable = timetable.reduce((acc, entry) => {
        if (!acc[entry.dan]) {
            acc[entry.dan] = [];
        }
        acc[entry.dan].push(entry);
        return acc;
    }, {});

    const getGridRow = (startTime) => {
        const timeSlots = {
            "08:00:00": 1,
            "08:50:00": 2,
            "09:40:00": 3,
            "10:30:00": 4,
            "11:30:00": 5,
            "12:20:00": 6,
            "13:10:00": 7,
        };
        return timeSlots[startTime] || 0;
    };

    const formatSubjectName = (name) => {
        let formattedName = name.replace(/_/g, " ");
        formattedName = formattedName.replace(/\s[^ ]*$/, "");
        return formattedName;
    };
    const formatDvorana = (name) => {
        return name.replace(/_/g, " ");
    };
    const formatSat = (time) => {
        let formattedTime = time.replace(/(:\d{2}){1}$/, "");
        return formattedTime;
    };

    return (
        <div className="timetable-container">
            {error && <div className="error-message">{error}</div>}
            <div className="timetable">
                {/* Empty Header for Time Column */}
                <div className="empty-header"></div>

                {/* Weekday Headers */}
                {Object.keys(dayMapping).map((dayKey) => (
                    <div key={dayKey} className="day-header">
                        {dayMapping[dayKey]}
                    </div>
                ))}

                {/* Time Column */}
                <div className="interval-column">
                    {timeIntervals.map((time, index) => (
                        <div key={index} className="interval-entry">{time}</div>
                    ))}
                </div>

                {/* Weekday Columns */}
                {Object.keys(dayMapping).map((dayKey) => (
                    <div key={dayKey} className="day-column">
                        {groupedTimetable[dayKey]?.map((entry, rowIndex) => (
                            <div
                                key={entry.predmet + rowIndex}
                                className="timetable-entry"
                                style={{
                                    gridRow: getGridRow(entry.pocetakSata),
                                }}
                            >
                                <div className="subject-ime"><strong>{formatSubjectName(entry.predmet)}</strong></div>
                                <div><strong>{`${formatSat(entry.pocetakSata)} - ${formatSat(entry.krajSata)}`}</strong>
                                </div>
                                <div><strong>{formatDvorana(entry.ucionica)}</strong></div>
                                <div>{entry.nastavnik}</div>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );


};

export default Timetable;
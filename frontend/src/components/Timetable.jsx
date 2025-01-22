import React, { useState, useEffect } from "react";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import "./Timetable.css";

const Timetable = ({ email }) => {
    const [timetable, setTimetable] = useState([]);
    const [error, setError] = useState(null);

    // Calculate the default week containing today's date in 2026
    const getCurrentWeek = () => {
        const startDate = new Date("2025-09-08"); // Adjust according to your calendar's week 1 start
        const today = new Date();
        today.setFullYear(2026); // Set year to 2026

        const diffInDays = Math.floor((today - startDate) / (1000 * 60 * 60 * 24));
        return Math.max(0, Math.floor(diffInDays / 7)); // Ensure non-negative week
    };

    const [currentWeek, setCurrentWeek] = useState(getCurrentWeek());

    const nonWorkingDates = [
        "2025-11-01", "2025-11-18", "2025-12-23", "2025-12-24", "2025-12-25", "2025-12-26",
        "2025-12-29", "2025-12-30", "2025-12-31",
        "2026-01-01", "2026-01-02", "2026-01-03", "2026-01-04", "2026-01-05", "2026-01-06",
        "2026-02-23", "2026-02-24", "2026-02-25", "2026-02-26", "2026-02-27",
        "2026-04-02", "2026-04-03", "2026-04-04", "2026-04-05", "2026-04-06",
        "2026-05-01", "2026-05-30"
    ];

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
                const response = await fetch(`/api/ucenik/${email}/raspored?week=${currentWeek}`);
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
    }, [email, currentWeek]);

    const dayMapping = {
        MONDAY: "Ponedjeljak",
        TUESDAY: "Utorak",
        WEDNESDAY: "Srijeda",
        THURSDAY: "Četvrtak",
        FRIDAY: "Petak",
    };

    const getDateForDay = (dayIndex) => {
        const startDate = new Date("2025-09-08");
        startDate.setDate(startDate.getDate() + dayIndex + (7 * currentWeek));
        return startDate;
    };

    const isNonWorkingDay = (date) => nonWorkingDates.includes(date.toISOString().split("T")[0]);

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

    const formatSubjectName = (name) => name.replace(/_/g, " ").replace(/\s[^ ]*$/, "");

    const formatDvorana = (name) => name.replace(/_/g, " ");

    const formatSat = (time) => time.replace(/(:\d{2}){1}$/, "");

    const goToPreviousWeek = () => setCurrentWeek((prev) => Math.max(prev - 1, 0));
    const goToNextWeek = () => setCurrentWeek((prev) => Math.min(prev + 1, 39));

    return (
        <div className="timetable-container">
            {error && <div className="error-message">{error}</div>}
            <div className="week-navigation">
                <button onClick={goToPreviousWeek} disabled={currentWeek === 0} className="week-button">
                    <FaArrowLeft/>
                </button>
                <span>{currentWeek + 1}. tjedan</span>
                <button onClick={goToNextWeek} disabled={currentWeek === 39} className="week-button">
                    <FaArrowRight/>
                </button>
            </div>
            <div className="timetable">
                <div className="empty-header"></div>
                {Object.keys(dayMapping).map((dayKey, index) => {
                    const date = getDateForDay(index);
                    return (
                        <div
                            key={dayKey}
                            className={`day-header ${isNonWorkingDay(date) ? "non-working" : ""}`}
                        >
                            {dayMapping[dayKey]} <br /> {date.toLocaleDateString()}
                        </div>
                    );
                })}
                <div className="interval-column">
                    {timeIntervals.map((time, index) => (
                        <div key={index} className="interval-entry">{time}</div>
                    ))}
                </div>
                {Object.keys(dayMapping).map((dayKey, index) => {
                    const date = getDateForDay(index);
                    return (
                        <div
                            key={dayKey}
                            className={`day-column ${isNonWorkingDay(date) ? "non-working" : ""}`}
                        >
                            {isNonWorkingDay(date) ? (
                                <div className="no-classes">Nema nastave</div>
                            ) : (
                                groupedTimetable[dayKey]?.map((entry, rowIndex) => (
                                    <div
                                        key={`${entry.predmet}-${rowIndex}`}
                                        className="timetable-entry"
                                        style={{ gridRow: getGridRow(entry.pocetakSata) }}
                                    >
                                        <div className="subject-ime"><strong>{formatSubjectName(entry.predmet)}</strong></div>
                                        <div><strong>{formatDvorana(entry.ucionica)}</strong></div>
                                        <div>{entry.nastavnik}</div>
                                    </div>
                                ))
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default Timetable;


import React, { useState, useEffect } from "react";
import { FaDownload, FaEnvelope } from "react-icons/fa";
import UcenikPredmeti from "./UcenikPredmeti";

const UcenikPotvrde = ({ userEmail }) => {
    const handleDownloadPotvrda = async () => {
        try {
            const response = await fetch(`/api/ucenik/${userEmail}/generirajPotvrdu`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/pdf',
                },
            });

            if (!response.ok) {
                throw new Error('Došlo je do greške pri preuzimanju PDF-a');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `potvrda_${userEmail}.pdf`);
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Greška pri preuzimanju potvrde:", error);
            alert("Došlo je do greške prilikom generiranja potvrde.");
        }
    };

    const handleEmailPotvrda = async () => {
        try {
            const response = await fetch(`/api/ucenik/${userEmail}/posaljiMail`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('Mail uspješno poslan!');
            } else {
                const errorMessage = await response.text();
                alert(`Greška pri slanju maila: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Došlo je do greške:', error);
            alert('Došlo je do greške pri slanju maila.');
        }
    };

    return (
        <div className="button-group">
            <button className="button" onClick={handleDownloadPotvrda}>
                <span className="button-text">Preuzmi potvrdu</span>
                <FaDownload className="button-icon" />
            </button>
            <button className="button" onClick={handleEmailPotvrda}>
                <span className="button-text">Pošalji potvrdu na mail</span>
                <FaEnvelope className="button-icon" />
            </button>
        </div>
    );
};

export default UcenikPotvrde;
import React from "react";

const UcioniceForm = () => {
    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            oznakaUc: formData.get("oznakaUc"),
            kapacitet: formData.get("kapacitet"),
        };

        try {
            const response = await fetch("/api/ravnatelj/ucionice/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            });

            if (!response.ok) throw new Error("Greška prilikom dodavanja učionice.");

            alert("Učionica dodana.");
            window.location.reload();
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

    return (
        <div className="form-container">
            <form onSubmit={handleSubmit} className="student-form">
                <div className="form-group">
                    <input type="text" name="oznakaUc" placeholder="Oznaka učionice" required />
                    <input type="number" name="kapacitet" placeholder="Kapacitet" required />
                </div>

                <button type="submit" className="add-button">
                    Dodaj učionicu
                </button>
            </form>
        </div>
    );
};

export default UcioniceForm;
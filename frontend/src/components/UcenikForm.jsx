import React from "react";
import "./UcenikForm.css";

const UcenikForm = ({ onUserAdded }) => {
    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            imeUcenik: formData.get("imeUcenik"),
            prezimeUcenik: formData.get("prezimeUcenik"),
            spol: formData.get("spol"),
            razred: formData.get("razred"),
            datumRodenja: formData.get("datumRodenja"),
            oib: formData.get("oib"),
            email: formData.get("email"),
            vjeronauk: formData.get("predmet") === "Vjeronauk", // true if Vjeronauk, false if Etika
        };

        try {
            const response = await fetch(`/api/admin/ucenik/add`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            });

            if (!response.ok) throw new Error("Greška prilikom dodavanja učenika.");

            alert("Učenik dodan.");
            if (onUserAdded) {
              onUserAdded();
          }
            
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

  return (
    <div className="form-container">
      <form onSubmit={handleSubmit} className="student-form">
        <div className="form-group">
          <input type="text" name="imeUcenik" placeholder="Ime" required /> 
          <input type="text" name="prezimeUcenik" placeholder="Prezime" required />
          <input type="email" name="email" placeholder="Email" required />  
          <input type="date" name="datumRodenja" required />
          <input type="text" name="oib" placeholder="OIB" required />
          <input type="text" name="spol" placeholder="Spol (M/Ž)" required />
          <input type="text" name="razred" placeholder="Razred" required />  
          <select name="predmet" required>
                        <option value="Vjeronauk">Vjeronauk</option>
                        <option value="Etika">Etika</option>
                    </select>
        </div>
        <button type="submit" className="add-button">
          Dodaj učenika
        </button>
        
      </form>
    </div>
  );
};

export default UcenikForm;
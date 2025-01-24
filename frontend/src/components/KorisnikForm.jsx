const KorisnikForm = ({ korisnik, onUserAdded }) => {
    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            ime: formData.get("ime"),
            prezime: formData.get("prezime"),
            email: formData.get("email"),
        };

        try {
            const response = await fetch(`/api/admin/${korisnik}/add`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            });

            if (!response.ok) throw new Error("Greška prilikom dodavanja korisnika.");

            alert(`${korisnik.charAt(0).toUpperCase() + korisnik.slice(1)} dodan.`);

           //fetch korisnike
           if (onUserAdded) {
            onUserAdded();
        }
        } catch (error) {
            alert(`Greška: ${error.message}`);
        }
    };

    return (
        <div className="form-container">

            <form onSubmit={handleSubmit} className="korisnik-form">
                <div className="form-group">
                    <input type="text" name="ime" placeholder="Ime" required />
                    <input type="text" name="prezime" placeholder="Prezime" required />
                    <input type="email" name="email" placeholder="Email" required />
                </div>
    
        <div>
        <button type="submit" className="add-button">
                        Dodaj {korisnik}a
                </button>
        </div>
               
            </form>
        </div>
        
    );
};
export default KorisnikForm;
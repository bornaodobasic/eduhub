import React from "react";

const Registration = () => {
    return (
        <div className="login-container">
            <form className="login-form">
                <div className="form-group">
                    <input type="text" placeholder="ime" required/>
                </div>
                <div className="form-group">
                    <input type="text" placeholder="prezime" required/>
                </div>
                <div className="form-group">
                    <input type="text" placeholder="email" required/>
                </div>
                <div className="form-group">
                    <input type="text" placeholder="lozinka" required/>
                </div>
                <button type="submit">REGISTRIRAJ SE</button>
            </form>
        </div>

    );
}

export default Registration;
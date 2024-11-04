import React from "react";
import './Enroll.css';

const Enroll = () => {
    return (
        <div className="outer-enroll-container">
            <div className="enroll">
                UPIS U PRVI RAZRED SREDNJE ŠKOLE
            </div>

            <div className="enroll-container">
                <form className="login-form">
                    <div className="form-group">
                        <input type="text" placeholder="ime" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="prezime" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="datum rodenja" required />
                    </div>
                    <div className="form-group">
                        <input type="text" placeholder="OIB" required />
                    </div>
                    <div className="form-group">
                        <select required defaultValue="">
                            <option value="" disabled>Odaberi željeni smjer</option>
                            <option value="opca">Opća gimnazija</option>
                            <option value="prirodoslovno-matematicka">Prirodoslovno-matematička gimnazija</option>
                            <option value="jezicna">Jezična gimnazija</option>
                        </select>
                    </div>
                    <button type="submit">PRIJAVA</button>
                </form>
            </div>
        </div>
    );
}

export default Enroll;

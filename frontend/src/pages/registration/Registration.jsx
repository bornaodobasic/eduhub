import React from "react";
import './Registration.css';

const Registration = () => {
    return (
        <div className="registration-container">
            <form className="registration-form">
                <div className="form-columns">
                    <div className="form-column">
                        <div className="form-group">
                            <input type="text" placeholder="podatak1" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak3" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak5" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak7" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak9" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak11" required />
                        </div>
                    </div>

                    <div className="form-column">
                        <div className="form-group">
                            <input type="text" placeholder="podatak2" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak4" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak6" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak8" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak10" required />
                        </div>
                        <div className="form-group">
                            <input type="text" placeholder="podatak12" required />
                        </div>
                    </div>
                </div>
                <button type="submit">REGISTRIRAJ SE</button>
            </form>
        </div>
    );
};

export default Registration;

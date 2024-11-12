import React from 'react';
import Header from '../../components/Header';
import './Certificate.css';

const Certificate = () => {

    return (
        <div className="certificate">
            <Header />

            <div className="content">
                <h1>Zahtjev za izdavanjem potvrda s elektroničkim pečatom</h1>
                <p>
                    Izdavanje potvrda s elektroničkim pečatom je omogućeno na način da korištenjem ove opcije 
                    pošaljete zahtjev za izdavanjem takve potvrde, a ona će vam biti poslana na vašu adresu elektroničke pošte.
                </p>

                <div className="certificate-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Vrsta potvrde</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Vrsta potvrde</td>
                                <td><button>Odaberi</button></td>
                            </tr>
                            <tr>
                                <td>Vrsta potvrde</td>
                                <td><button>Odaberi</button></td>
                            </tr>
                            <tr>
                                <td>Vrsta potvrde</td>
                                <td><button>Odaberi</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <button className="pregledZahtjeva-button">Pregled zahtjeva</button>
            </div>
        </div>
    );
};

export default Certificate;

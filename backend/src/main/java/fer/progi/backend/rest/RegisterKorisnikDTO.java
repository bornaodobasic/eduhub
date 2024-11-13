package fer.progi.backend.rest;

import lombok.Data;

@Data
public class RegisterKorisnikDTO {
 	private String imeKorisnik;
    private String prezimeKorisnik;
    private String email;
    private String lozinka;
    private String role;
}

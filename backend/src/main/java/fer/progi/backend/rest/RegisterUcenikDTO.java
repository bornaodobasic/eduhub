package fer.progi.backend.rest;

import fer.progi.backend.domain.Razred;
import lombok.Data;

@Data
public class RegisterUcenikDTO {
	
	private String oib;
    private String imeUcenik;
    private String prezimeUcenik;
    private String datumRodenja;
    private String spol;
    private Razred razred;
    private String email;
    private String lozinka;
}

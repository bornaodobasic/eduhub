package fer.progi.backend.rest;

import lombok.Data;

@Data
public class RegisterUcenikDTO {
	private String oib;
    private String imeUcenik;
    private String prezimeUcenik;
    private String spol;
    private String email;
    private String lozinka;
}

package fer.progi.backend.rest;

import fer.progi.backend.domain.Razred;
import lombok.Data;

@Data
public class RegisterUcenikDTO {
	
    private String imeUcenik;
    private String prezimeUcenik;
    private String email;
    private String spol;
}

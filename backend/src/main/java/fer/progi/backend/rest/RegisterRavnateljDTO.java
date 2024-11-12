package fer.progi.backend.rest;

import lombok.Data;

@Data
public class RegisterRavnateljDTO {
	private String imeRavnatelj;
	private String prezimeRavnatelj;
	private String email;
	private String lozinka;

}

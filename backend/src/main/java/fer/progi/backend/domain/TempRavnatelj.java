package fer.progi.backend.domain;

import jakarta.persistence.Id;

public class TempRavnatelj {
	
	private String imeRavnatelj;
	private String prezimeRavnatelj;
	
	@Id
	private String email;
	private String lozinka;

}

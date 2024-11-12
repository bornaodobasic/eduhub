package fer.progi.backend.domain;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class TempRavnatelj {
	
	private String imeRavnatelj;
	private String prezimeRavnatelj;
	
	@Id
	private String email;
	private String lozinka;

}

package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempRavnatelj {
	
	private String imeRavnatelj;
	private String prezimeRavnatelj;
	
	@Id
	private String email;
	private String lozinka;

}

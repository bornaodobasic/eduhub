package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempUcenik {
	
	@Id
	private String oib;
	
    private String imeUcenik;
    private String prezimeUcenik;
    private String spol;
    private String email;
    private String lozinka;
    private Razred razred;
}

package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ucionica {
    
	@Id
	private String oznakaUc;
	
	private Integer kapacitet;
	
	
}

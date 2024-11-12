package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempSatnicar {
	
	private String imeSatnicar;
	private String prezimeSatnicar;
	
	@Id
	private String email;
	private String lozinka;

}

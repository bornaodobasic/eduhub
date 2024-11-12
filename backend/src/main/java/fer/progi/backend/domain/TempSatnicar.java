package fer.progi.backend.domain;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class TempSatnicar {
	
	private String imeSatnicar;
	private String prezimeSatnicar;
	
	@Id
	private String email;
	private String lozinka;

}

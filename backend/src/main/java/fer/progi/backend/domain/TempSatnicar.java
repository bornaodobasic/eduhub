package fer.progi.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class TempSatnicar {
	
	private String imeSatnicar;
	private String prezimeSatnicar;
	
	@Id
	private String email;
	private String lozinka;

}

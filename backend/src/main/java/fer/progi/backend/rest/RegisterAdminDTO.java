package fer.progi.backend.rest;

import lombok.Data;

@Data
public class RegisterAdminDTO {
	
	 	private String imeAdmin;
	    private String prezimeAdmin;
	    private String email;
	    private String lozinka;
	}

package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {
	
	@Id
	private int sifAdmin;
	
	private String imeAdmin;
	private String prezimeAdmin;
	
	private String email;
	private String lozinka;
	
}

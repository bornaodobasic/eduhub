package fer.progi.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ravnatelj {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer sifRavnatelj;
	
	private String imeRavnatelj;
	private String prezimeRavnatelj;
	
	@Column(unique=true)
	private String email;
	private String lozinka;
}

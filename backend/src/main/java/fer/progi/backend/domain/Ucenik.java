package fer.progi.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Ucenik {
	
	@Id
	@Size(min=11, max=11)
	private String oib;
	
	private String imeUcenik;
	private String prezimeUcenik;
	private String datumRodenja;
	@Size(min=1, max=1)
	private String spol;	
	
	@Column(unique=true)
	private String email;
	private String lozinka;
	
	@ManyToOne
	private Razred razred;
	
	@ManyToOne
	private Aktivnost aktivnost;
	
}

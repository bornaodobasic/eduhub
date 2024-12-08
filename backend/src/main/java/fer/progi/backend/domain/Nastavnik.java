package fer.progi.backend.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Nastavnik {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String imeNastavnik;
	private String prezimeNastavnik;
	
	@Column(unique=true)
	private String email;
	
	@ManyToMany(mappedBy = "nastavnici")
	private Set<Predmet> predmeti;
	
}

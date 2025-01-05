package fer.progi.backend.domain;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
	
	@ManyToMany(mappedBy = "nastavnici", fetch = FetchType.EAGER)
	private Set<Predmet> predmeti;
	
}

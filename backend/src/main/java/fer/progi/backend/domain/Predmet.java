package fer.progi.backend.domain;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer sifPredmet;
	
	private String nazPredmet;
	private Integer ukBrSatiTjedno;
	
	@ManyToOne
	private Smjer smjer;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "predmet_nastavnik",
			joinColumns = @JoinColumn(name = "sifPredmet"),
			inverseJoinColumns = @JoinColumn(name = "sifNastavnik"))
	private Set<Nastavnik> nastavnici;

}

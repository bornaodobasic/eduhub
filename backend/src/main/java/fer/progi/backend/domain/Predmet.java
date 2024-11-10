package fer.progi.backend.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer sifPredmet;
	
	private String nazPredmet;
	private int ukBrSatiTjedno;
	
	@ManyToOne
	private Smjer smjer;
	
	@ManyToMany
	@JoinTable(
			name = "predmet_nastavnik",
			joinColumns = @JoinColumn(name = "sifPredmet"),
			inverseJoinColumns = @JoinColumn(name = "sifNastavnik"))
	private Set<Nastavnik> nastavnici;
	

}

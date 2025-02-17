package fer.progi.backend.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	@JsonBackReference
	private Smjer smjer;

	/*
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "predmet_nastavnik",
			joinColumns = @JoinColumn(name = "sifPredmet"),
			inverseJoinColumns = @JoinColumn(name = "sifNastavnik"))
	@JsonManagedReference
	private List<Nastavnik> nastavnici;
*/

	@ManyToMany(mappedBy = "predmeti", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Nastavnik> nastavnici;

	@Override
	public String toString() {
		return "Predmet{" +
				"id=" + sifPredmet +
				", nazivPredmet='" + nazPredmet + '\'' +
				'}';
	}
	
    @OneToMany(mappedBy = "predmet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Obavijest> obavijesti;


}

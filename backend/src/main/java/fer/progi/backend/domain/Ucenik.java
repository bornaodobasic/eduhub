package fer.progi.backend.domain;

import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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
	
	@ManyToMany
	@JoinTable(
			name = "ucenik_aktivnost",
			joinColumns = @JoinColumn(name = "oib"),
			inverseJoinColumns = @JoinColumn(name = "sifAktivnost"))
	private Set<Aktivnost> aktivnosti = new HashSet<>();
	
	@Transient
	private Set<Integer> sifreAktivnost;
	
}

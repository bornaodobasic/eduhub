package fer.progi.backend.domain;

import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
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

	@GeneratedValue
	@Id
	private int id;
	
	private String imeUcenik;
	private String prezimeUcenik;

	@Size(min=1, max=1)
	private String spol;	
	
	@Column(unique=true)
	private String email;

	@ManyToOne
	private Razred razred;
	
	@ManyToMany
	@JoinTable(
			name = "ucenik_aktivnost",
			joinColumns = @JoinColumn(name = "email"),
			inverseJoinColumns = @JoinColumn(name = "sifAktivnost"))
	private Set<Aktivnost> aktivnosti = new HashSet<>();
	
	@Transient
	private Set<Integer> sifreAktivnost;
	
}

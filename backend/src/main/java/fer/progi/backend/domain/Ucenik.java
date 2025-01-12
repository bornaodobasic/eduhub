package fer.progi.backend.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	private Integer id;
	
	private String imeUcenik;
	private String prezimeUcenik;

	private String oib;
	private String datumRodenja;

	@ManyToOne
	@JsonBackReference
	private Razred razred;

	private String spol;	
	
	@Column(unique=true)
	private String email;
	
	@ManyToMany(cascade = jakarta.persistence.CascadeType.MERGE, fetch = jakarta.persistence.FetchType.EAGER)
	@JoinTable(
			name = "ucenik_aktivnosti",
			joinColumns = @JoinColumn(name = "id"),
		inverseJoinColumns = @JoinColumn(name = "sifAktivnost"))
	@JsonManagedReference
	private List<Aktivnost> aktivnosti;

	//@Transient
	//private Set<Integer> sifreAktivnost;
	
}

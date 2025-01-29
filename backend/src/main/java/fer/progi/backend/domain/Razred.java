package fer.progi.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "razrednik")
public class Razred implements Serializable {
	
	@Id
	@Column(length = 2)
	private String nazRazred;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Smjer smjer;

	@OneToMany(mappedBy = "razred", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
    private Set<Ucenik> ucenici;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "razrednik_id", referencedColumnName = "id", unique = true)
	@JsonManagedReference
	private Nastavnik razrednik;
}

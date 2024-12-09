package fer.progi.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Entity
@Data
public class Razred implements Serializable {
	
	@Id
	@Column(length = 2)
	private String nazRazred;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Smjer smjer;

	@OneToMany(mappedBy = "razred", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ucenik> ucenici;
}

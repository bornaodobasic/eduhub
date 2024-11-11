package fer.progi.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Razred {
	
	@Id
	@Size(min=2, max=2)
	private String nazRazred;
	
	@ManyToOne
	private Smjer smjer;
	
    @OneToMany(mappedBy = "razred", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ucenik> ucenici;
}

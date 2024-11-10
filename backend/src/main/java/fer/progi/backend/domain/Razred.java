package fer.progi.backend.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Razred {
	
	@Id
	@Size(min=2, max=2)
	private String nazRazred;
	
	@ManyToOne
	private Smjer smjer;
	
    @OneToMany(mappedBy = "razred", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ucenik> ucenici;
}

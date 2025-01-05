package fer.progi.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	@JsonBackReference
	private Smjer smjer;

	@OneToMany(mappedBy = "razred", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
    private Set<Ucenik> ucenici;
}

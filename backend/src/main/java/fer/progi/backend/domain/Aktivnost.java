package fer.progi.backend.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Aktivnost {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sifAktivnost;
	private String oznAktivnost;
	
    @OneToMany(mappedBy = "aktivnost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ucenik> ucenici;
}

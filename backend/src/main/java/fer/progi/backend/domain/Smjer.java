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
public class Smjer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sifSmjer;
    
    private String nazivSmjer;
    
    @OneToMany(mappedBy = "smjer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Predmet> predmeti;
}

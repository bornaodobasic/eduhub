package fer.progi.backend.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Aktivnost {
	
	@GeneratedValue
	@Id
	private int sifAktivnost;
	
	private String oznAktivnost;
	
   @ManyToMany(mappedBy = "aktivnosti", fetch = FetchType.EAGER)
   @JsonBackReference
   private List<Ucenik> ucenici;
    
    
}

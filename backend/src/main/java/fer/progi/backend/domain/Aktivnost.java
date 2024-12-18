package fer.progi.backend.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Aktivnost {
	
	@Id
	@GeneratedValue
	private int sifAktivnost;
	
	private String oznAktivnost;
	
   @ManyToMany(mappedBy = "aktivnosti")
   private Set<Ucenik> ucenici; 
    
    
}

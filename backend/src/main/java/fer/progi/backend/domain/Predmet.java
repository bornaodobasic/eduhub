package fer.progi.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sifPredmet;
	
	private String nazPredmet;
	private int ukBrSatiTjedno;
	
	@ManyToOne
    //@JoinColumn(name = "sifSmjer")
	private Smjer smjer;
	
	//jel se vidi promjena
}

package fer.progi.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Satnicar {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sifSatnicar;
	
	private String imeSatnicar;
	private String prezimeSatnicar;
	
	@Column(unique=true)
	private String email;
	private String lozinka;
}

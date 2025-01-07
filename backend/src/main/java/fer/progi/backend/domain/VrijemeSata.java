package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class VrijemeSata {

	@Id
	@GeneratedValue
	private Integer id;

	private String dan;
	private String pocetakSata;
	private String krajSata;

}

package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
public class VrijemeSata {

	@Id
	@GeneratedValue
	private Integer id;

	private DayOfWeek dan;
	private LocalTime pocetakSata;
	private LocalTime krajSata;

}

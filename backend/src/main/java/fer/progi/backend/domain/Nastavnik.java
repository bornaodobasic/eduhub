package fer.progi.backend.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Nastavnik {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String imeNastavnik;
	private String prezimeNastavnik;
	
	@Column(unique=true)
	private String email;
	
	@ManyToMany(mappedBy = "nastavnici", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Predmet> predmeti;

	@Override
	public String toString() {
		return "Nastavnik{" +
				"id=" + id +
				", imeNastavnik='" + imeNastavnik + '\'' +
				", prezimeNastavnik='" + prezimeNastavnik + '\'' +
				", email='" + email + '\'' +
				'}';
	}
	
}

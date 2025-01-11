package fer.progi.backend.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	
	//@Column(unique=true)
	private String email;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "predmet_nastavnik",
			joinColumns = @JoinColumn(name = "sifNastavnik"),
			inverseJoinColumns = @JoinColumn(name = "sifPredmet"))
	@JsonManagedReference
	private List<Predmet> predmeti;
	/*
	@ManyToMany(mappedBy = "nastavnici", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Predmet> predmeti;
*/
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

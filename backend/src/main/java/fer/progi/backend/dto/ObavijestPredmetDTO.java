package fer.progi.backend.dto;

import java.util.Date;

import lombok.Data;

@Data 
public class ObavijestPredmetDTO {
	
	private String naslov;
	private String sadrzaj;
	private String imeNastavnik;
	private String prezimeNastavnik;
	private Date datum;
	
	public ObavijestPredmetDTO(String naslov, String sadrzaj, String imeNastavnik, String prezimeNastavnik,
			Date datum) {
		super();
		this.naslov = naslov;
		this.sadrzaj = sadrzaj;
		this.imeNastavnik = imeNastavnik;
		this.prezimeNastavnik = prezimeNastavnik;
		this.datum = datum;
	}
	
	

}

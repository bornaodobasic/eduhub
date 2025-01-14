package fer.progi.backend.rest;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ObavijestDTO {

	
	  private String naslov;
	    private String sadrzaj;
	    private String odredisteAdresa;
	    private String gradOdrediste;
	    private String drzavaOdrediste;
	    
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
	    private Date datum;

	    public ObavijestDTO(String naslov, String sadrzaj, String odredisteAdresa, String gradOdrediste, String drzavaOdrediste, Date datum) {
	        this.naslov = naslov;
	        this.sadrzaj = sadrzaj;
	        this.odredisteAdresa = odredisteAdresa;
	        this.gradOdrediste = gradOdrediste;
	        this.drzavaOdrediste = drzavaOdrediste;
	        this.datum = datum;
	    }
	
	

}

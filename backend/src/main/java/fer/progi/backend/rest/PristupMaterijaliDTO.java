package fer.progi.backend.rest;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PristupMaterijaliDTO {
	

	private String imeUcenik;
	private String prezimeUcenik;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
	private Date datum;
	
	private String imeMaterijal;
	private String iemPredmet;
	

	public PristupMaterijaliDTO(String imeUcenik, String prezimeUcenik, Date datum, String imeMaterijal,
			String iemPredmet) {
		super();
		this.imeUcenik = imeUcenik;
		this.prezimeUcenik = prezimeUcenik;
		this.datum = datum;
		this.imeMaterijal = imeMaterijal;
		this.iemPredmet = iemPredmet;
	}

}

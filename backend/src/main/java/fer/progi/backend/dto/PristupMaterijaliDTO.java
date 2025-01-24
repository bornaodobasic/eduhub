package fer.progi.backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PristupMaterijaliDTO {


	private String imeUcenik;
	private String prezimeUcenik;

	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
	private Date datum;

	private String imeMaterijal;
	private String iemPredmet;

	public PristupMaterijaliDTO(String imeUcenik, String prezimeUcenik, String email, Date datum, String imeMaterijal,
								String iemPredmet) {
		super();
		this.imeUcenik = imeUcenik;
		this.prezimeUcenik = prezimeUcenik;
		this.email = email;
		this.datum = datum;
		this.imeMaterijal = imeMaterijal;
		this.iemPredmet = iemPredmet;
	}




}
package fer.progi.backend.rest;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ZahtjeviDTO {
	
	private String imeUcenik;
	private String prezimeUcenik;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
	private  Date datumGeneriranja;
	
	
	public ZahtjeviDTO(String imeUcenik, String prezimeUcenik, Date date) {
		super();
		this.imeUcenik = imeUcenik;
		this.prezimeUcenik = prezimeUcenik;
		this.datumGeneriranja = date;
	}
	
	
	

}

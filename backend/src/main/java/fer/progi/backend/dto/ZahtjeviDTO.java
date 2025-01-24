package fer.progi.backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ZahtjeviDTO {

	private String imeUcenik;
	private String prezimeUcenik;
	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Zagreb")
	private  Date datumGeneriranja;

	public ZahtjeviDTO(String imeUcenik, String prezimeUcenik, String email, Date datumGeneriranja) {
		super();
		this.imeUcenik = imeUcenik;
		this.prezimeUcenik = prezimeUcenik;
		this.email = email;
		this.datumGeneriranja = datumGeneriranja;
	}







}
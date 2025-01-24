package fer.progi.backend.dto;

import fer.progi.backend.domain.Smjer;
import lombok.Data;

@Data
public class UpisDTO {
	
    private String imeUcenik;
    private String prezimeUcenik;
    private String spol;
    private String smjer;
    private String datumRodenja;
    private String oib;
    private Boolean vjeronauk;
    

}

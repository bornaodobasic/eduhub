package fer.progi.backend.rest;

import lombok.Data;

@Data
public class AdminAddUcenikDTO {

    private String imeUcenik;
    private String prezimeUcenik;
    private String spol;
    private String razred;
    private String datumRodenja;
    private String oib;
    private String email;
}

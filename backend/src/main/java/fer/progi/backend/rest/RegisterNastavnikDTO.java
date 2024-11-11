package fer.progi.backend.rest;

import lombok.Data;

@Data
public class RegisterNastavnikDTO {
    private String imeNastavnik;
    private String prezimeNastavnik;
    private String email;
    private String lozinka;
}

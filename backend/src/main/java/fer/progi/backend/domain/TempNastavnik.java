package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempNastavnik {
    private String imeNastavnik;
    private String prezimeNastavnik;

    @Id
    private String email;

    private String lozinka;
}

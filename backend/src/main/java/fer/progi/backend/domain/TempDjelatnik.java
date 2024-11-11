package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempDjelatnik {
    private String imeDjelatnik;
    private String prezimeDjelatnik;

    @Id
    private String email;

    private String lozinka;
}

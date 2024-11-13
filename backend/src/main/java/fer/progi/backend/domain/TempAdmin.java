package fer.progi.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TempAdmin {
    private String imeAdmin;
    private String prezimeAdmin;

    @Id
    private String email;

    private String lozinka;
}


package fer.progi.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RazredPredmetNastavnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Razred razred;

    @ManyToOne
    private Predmet predmet;

    @ManyToOne
    private Nastavnik nastavnik;
}

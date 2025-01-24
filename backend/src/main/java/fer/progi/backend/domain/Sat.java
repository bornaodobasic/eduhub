package fer.progi.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private RazredPredmetNastavnik rpn;

    @ManyToOne
    private VrijemeSata vrijemeSata;

    @ManyToOne
    private Ucionica ucionica;

}

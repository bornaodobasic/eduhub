package fer.progi.backend.rest;

import lombok.Data;

import java.util.List;

@Data
public class PredmetMaterijaliDTO {

    private Integer sifPredmet;
    private String nazPredmet;
    private List<String> materijali;

    public PredmetMaterijaliDTO(Integer sifPredmet, String nazPredmet, List<String> materijali) {
        this.sifPredmet = sifPredmet;
        this.nazPredmet = nazPredmet;
        this.materijali = materijali;
    }
}

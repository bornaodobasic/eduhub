package fer.progi.backend.service;

import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.RazredPredmetNastavnik;

import java.util.List;

public interface RazredPredmetNastavnikService {
    void dodijeliNastavnike();

    List<RazredPredmetNastavnik> listAll();

    void sati();

    RazredPredmetNastavnik findRP(Razred razred, Predmet predmet);

    void saveAll(List<RazredPredmetNastavnik> razredPredmetNastavnik);

    List<RazredPredmetNastavnik> findByNastavnik(Nastavnik nastavnik);
}

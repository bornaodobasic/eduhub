package fer.progi.backend.service;

import fer.progi.backend.domain.RazredPredmetNastavnik;

import java.util.List;

public interface RazredPredmetNastavnikService {
    void dodijeliNastavnike();

    List<RazredPredmetNastavnik> listAll();

    void sati();
}

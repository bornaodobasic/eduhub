package fer.progi.backend.dao;

import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.RazredPredmetNastavnik;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RazredPredmetNastavnikRepository extends JpaRepository<RazredPredmetNastavnik, Integer> {

    RazredPredmetNastavnik findByRazredAndPredmet(Razred razred, Predmet predmet);
    List<RazredPredmetNastavnik> findByNastavnik(Nastavnik nastavnik);
    List<RazredPredmetNastavnik> findByRazred(Razred razred);

}

package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import fer.progi.backend.domain.Predmet;
import java.util.List;
import java.util.Set;

public interface PredmetRepository extends JpaRepository<Predmet, Integer>{
	int countBySifPredmet(Integer sifPredmet);

	Predmet findByNazPredmet(String nazPredmet);
	Set<Predmet> findByNazPredmetIn(List<String> nazPredmet);

	boolean existsByNazPredmet(String nazPredmet);

}

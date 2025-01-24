package fer.progi.backend.dao;

import fer.progi.backend.domain.Smjer;
import org.springframework.data.jpa.repository.JpaRepository;
import fer.progi.backend.domain.Predmet;
import java.util.List;

public interface PredmetRepository extends JpaRepository<Predmet, Integer>{

	Predmet findByNazPredmet(String nazPredmet);
	List<Predmet> findByNazPredmetIn(List<String> nazPredmet);

	boolean existsByNazPredmet(String nazPredmet);

	Predmet findBySifPredmet(Integer sifPredmet);

	boolean existsByNazPredmetAndSmjer(String nazPredmet, Smjer smjer);
}

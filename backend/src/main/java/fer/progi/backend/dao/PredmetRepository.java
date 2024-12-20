package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Predmet;

public interface PredmetRepository extends JpaRepository<Predmet, Integer>{
	int countBySifPredmet(Integer sifPredmet);

	Predmet findByNazPredmet(String nazPredmet);

}

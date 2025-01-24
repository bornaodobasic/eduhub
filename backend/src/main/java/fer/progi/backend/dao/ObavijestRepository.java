package fer.progi.backend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Obavijest;

public interface ObavijestRepository extends JpaRepository<Obavijest, Integer> {

	 List<Obavijest> findByPredmetNazPredmet(String nazPredmet);
	 List<Obavijest> findByPredmetIsNull();
	 List<Obavijest> findByAdresaLokacijaIsNull();


	
}

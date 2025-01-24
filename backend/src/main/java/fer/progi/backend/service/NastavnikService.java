package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.dto.RasporedDTO;

public interface NastavnikService {
	
	List<Nastavnik> listAll();
	
	Nastavnik dodajNastavnik(Nastavnik nastavnik);

	Nastavnik findBySifNastavnik(Integer sifNastavnik);

	boolean createIfNeeded(AddDTO addDTO);

	List<Nastavnik> findAllNastavniks();
	
	boolean deleteNastavnik(String email);

	List<Predmet> findNastavnikPredmeti(String email);

	Optional<Nastavnik> findByEmail(String email);


}

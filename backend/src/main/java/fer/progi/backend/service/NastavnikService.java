package fer.progi.backend.service;

import java.util.List;

import java.util.Set;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;

public interface NastavnikService {
	
	List<Nastavnik> listAll();
	
	Nastavnik dodajNastavnik(Nastavnik nastavnik);

	Nastavnik findBySifNastavnik(Integer sifNastavnik);

	boolean createIfNeeded(String email);

	List<Nastavnik> findAllNastavniks();
	
	boolean deleteNastavnik(String email);

	List<Predmet> findNastavnikPredmeti(String email);
}

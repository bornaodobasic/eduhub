package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Nastavnik;

public interface NastavnikService {
	
	List<Nastavnik> listAll();
	
	Nastavnik dodajNastavnik(Nastavnik nastavnik);

	Nastavnik findBySifNastavnik(Integer sifNastavnik);

	boolean createIfNeeded(String email);

}

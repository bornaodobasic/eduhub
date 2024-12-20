package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Predmet;

public interface PredmetService {
	
	List<Predmet> listAll();
	
	void dodajNastavnikaUPredmet(Integer sifPredmet, Integer sifNastavnik);

	Predmet findPredmetByNaziv(String string);
}

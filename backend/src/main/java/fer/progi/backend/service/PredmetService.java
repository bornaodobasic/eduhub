package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Predmet;

public interface PredmetService {
	
	List<Predmet> listAll();
	
	//Predmet dodajPredmet(Predmet predmet);
	
	Predmet addPredmet(Predmet predmet);
	
	void dodajNastavnikaUPredmet(Integer sifPredmet, Integer sifNastavnik);
}

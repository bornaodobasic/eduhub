package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;

public interface RazredService {
	
	List<Razred> listAll();
	
	Razred getBestClass(String string);

	boolean findByNazRazred(String nazRazred);

	void dodajRazred(String nazRazred, String nazivSmjer);

	Razred findRazred(String nazRazred);

	Razred findByNazivRazred(String nazRazred);
}

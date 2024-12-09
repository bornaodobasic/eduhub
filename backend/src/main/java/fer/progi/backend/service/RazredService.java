package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;

public interface RazredService {
	
	List<Razred> listAll();
	
	Razred addRazred(Razred razred);
	
	Razred getBestClass(String string);

	Razred findByNazRazred(String nazRazred);

}

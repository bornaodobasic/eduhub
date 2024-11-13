package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Razred;

public interface RazredService {
	
	List<Razred> listAll();
	
	Razred addRazred(Razred razred);
	
	//Razred dodajRazred(Razred razred);

	Razred findByNazRazred(String nazRazred);

}
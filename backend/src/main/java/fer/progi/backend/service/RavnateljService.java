package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Ravnatelj;


public interface RavnateljService {
	
	List<Ravnatelj> listAll();
	
	Ravnatelj dodajRavnatelj(Ravnatelj ravnatelj);
	

}

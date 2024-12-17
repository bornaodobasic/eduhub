package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Ravnatelj;


public interface RavnateljService {
	
	List<Ravnatelj> listAll();
	
	Ravnatelj dodajRavnatelj(Ravnatelj ravnatelj);

	boolean createIfNeeded(String email);

	List<Ravnatelj> findAllRavnateljs();
	void deleteRavnatelj(String email);
	

}

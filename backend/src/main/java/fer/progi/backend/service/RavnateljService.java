package fer.progi.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.dto.AddDTO;


public interface RavnateljService {
	
	List<Ravnatelj> listAll();
	
	Ravnatelj dodajRavnatelj(Ravnatelj ravnatelj);

	boolean createIfNeeded(AddDTO addDTO);
	List<Ravnatelj> findAllRavnateljs();
	void deleteRavnatelj(String email);

	

}

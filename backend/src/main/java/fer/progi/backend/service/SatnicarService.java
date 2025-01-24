package fer.progi.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.domain.Satnicar;


public interface SatnicarService {
	
	List<Satnicar> listAll();
	
	Satnicar dodajSatnicara (Satnicar satnicar);

	boolean createIfNeeded(AddDTO addDTO);

	List<Satnicar> findAllSatnicars();
	void deleteSatnicar(String email);

	Map<String, Double> pregledZauzecaUcionica();
	

}

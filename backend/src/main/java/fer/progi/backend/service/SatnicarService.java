package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.rest.AddDTO;


public interface SatnicarService {
	
	List<Satnicar> listAll();
	
	Satnicar dodajSatnicara (Satnicar satnicar);

	boolean createIfNeeded(AddDTO addDTO);

	List<Satnicar> findAllSatnicars();
	void deleteSatnicar(String email);
	

}

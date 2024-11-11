package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.Satnicar;


public interface SatnicarService {
	
	List<Satnicar> listAll();
	
	Satnicar dodajSatnicara (Satnicar satnicar);
	

}
